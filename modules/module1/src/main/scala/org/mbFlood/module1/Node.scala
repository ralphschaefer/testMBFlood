package org.mbFlood.module1


import akka.actor.{ActorRef, ActorSystem, CoordinatedShutdown, Props}
import akka.routing.FromConfig
import com.typesafe.config.Config
import org.mbFlood.module1.actors.{ConsumeMessage, SpreadMessage}
import org.mbFlood.actors.Shutdown

trait Node {
  val system : ActorSystem
  val config : Config
  val spreadActor: ActorRef
  val shutdownActor: ActorRef
  val consumerActor: ActorRef
  def shutdown():Unit = {
    CoordinatedShutdown.get(system).run(CoordinatedShutdown.unknownReason)
    System.exit(0)
  }

}

object Node {

  class ShutdownActor(funShutdown: ()=>Unit) extends Shutdown {
    def shutdown: Unit = funShutdown()
  }

  def apply(actorSystemName: String): Node = new Node {
    val system: ActorSystem = ActorSystem(actorSystemName)
    val consumerActor: ActorRef = system.actorOf(ConsumeMessage.props.withMailbox("bounded-mailbox"),
      "consumeActor")
    val shutdownActor: ActorRef = system.actorOf(Props(new ShutdownActor(shutdown)),Shutdown.path)
    val spreadActor: ActorRef = system.actorOf(SpreadMessage.props,"spreadActor")
    lazy val config = system.settings.config
  }
}

