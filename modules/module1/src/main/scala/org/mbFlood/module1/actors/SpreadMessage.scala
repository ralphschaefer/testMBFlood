package org.mbFlood.module1.actors

import akka.actor.{Actor, DeadLetter, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.MemberEvent
import akka.routing.FromConfig
import org.mbFlood.messages.{SimpleMessage, Spread}
import org.mbFlood.module1.Node

class SpreadMessage extends Actor {

  val cluster = Cluster(context.system)

  override def preStart(): Unit = {
    cluster.subscribe(self, classOf[MemberEvent])
    context.system.eventStream.subscribe(self,  classOf[DeadLetter])
  }

  override def postStop(): Unit = cluster.unsubscribe(self)

  val consumerRoute = context.system.actorOf(FromConfig.getInstance().props(), "consumerRoute")

  def receive: Receive = {
    case Spread(msg,factor) =>
      // send message many times to Sender
      println(s"spread $msg $factor times")
      (1 to factor).foreach( i => consumerRoute ! SimpleMessage(s"$msg: $i") )
    case m:MemberEvent =>
      println("member Event: " + m)
    case d:DeadLetter =>
      self ! d.message
    case SimpleMessage(msg) =>
      println("not procesed message : "+msg)

  }
}

object SpreadMessage {
  def props = Props(new SpreadMessage)
}

