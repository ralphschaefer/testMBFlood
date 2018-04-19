package org.mbFlood.actors

import akka.actor.Actor
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.MemberEvent
import org.mbFlood.messages.MessageShutdown

trait Shutdown extends Actor {

  val cluster = Cluster(context.system)

  override def preStart(): Unit = cluster.subscribe(self,classOf[MemberEvent])

  override def postStop(): Unit = cluster.unsubscribe(self)

  def shutdown:Unit

  def receive: Receive = {
    case MessageShutdown =>
      println("*******************************")
      println("**** Node remote force shutdown")
      println("*******************************")
      shutdown
    case _:MemberEvent =>
  }
}

object Shutdown {
  val path = "shutdown"
}