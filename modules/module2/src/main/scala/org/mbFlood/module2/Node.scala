package org.mbFlood.module2


import akka.actor.{ActorRef, ActorSystem, CoordinatedShutdown, Props}
import akka.routing.FromConfig
import com.typesafe.config.Config
import org.mbFlood.actors.Shutdown

trait Node {
  val system : ActorSystem
  val config : Config
  val spreadRoute: ActorRef
  val shutdownActor: ActorRef
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
    lazy val config = system.settings.config
    val spreadRoute: ActorRef = system.actorOf(FromConfig.getInstance().props(), "spreadRoute")
    val shutdownActor: ActorRef = system.actorOf(Props(new ShutdownActor(shutdown)),Shutdown.path)
  }
}

