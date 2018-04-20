package org.mbFlood.module1.actors

import akka.actor.{Actor, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.MemberEvent
import org.mbFlood.messages.{SimpleMessage, Spread}

class ConsumeMessage extends Actor {

  val cluster = Cluster(context.system)

  override def preStart(): Unit = {
    cluster.subscribe(self, classOf[MemberEvent])
  }

  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive = {
    case SimpleMessage(msg) =>
       print(msg)
       print(" ---- > ")
       Thread.sleep(100)
       println("*")
    case _:MemberEvent =>

  }

}

object ConsumeMessage {
  def props = Props(new ConsumeMessage)
}