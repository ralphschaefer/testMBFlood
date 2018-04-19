package org.mbFlood.module1.actors

import akka.actor.{Actor, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.MemberEvent
import org.mbFlood.messages.{SimpleMessage, Spread}

class SpreadMessage extends Actor {

  val cluster = Cluster(context.system)

  override def preStart(): Unit = cluster.subscribe(self,classOf[MemberEvent])

  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive: Receive = {
    case Spread(msg,factor) =>
      // send message many times to Sender
      println(s"spread $msg $factor times")
      // (1 to factor).foreach( i => sender() ! SimpleMessage(s"$msg: $i") )
    case m:MemberEvent =>
      println("member Event: " + m)
  }
}

object SpreadMessage {
  def props = Props(new SpreadMessage)
}

