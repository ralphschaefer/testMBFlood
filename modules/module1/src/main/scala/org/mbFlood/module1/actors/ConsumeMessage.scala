package org.mbFlood.module1.actors

import akka.actor.{Actor, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.MemberEvent
import org.mbFlood.messages.{SimpleMessage, Spread}
import org.mbFlood.module1.actors.SpreadMessage.Akn

class ConsumeMessage extends Actor {

  val cluster = Cluster(context.system)

  override def preStart(): Unit = {
    cluster.subscribe(self, classOf[MemberEvent])
  }

  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive = {
    case item@SimpleMessage(msg,origin,_) =>
      print(msg)
      print(" ---- > ")
      Thread.sleep(100)
      println("*")
      sender ! Akn(item)
    case _:MemberEvent =>

  }

}

object ConsumeMessage {
  def props = Props(new ConsumeMessage)
}