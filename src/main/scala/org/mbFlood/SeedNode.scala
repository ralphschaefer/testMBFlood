package org.mbFlood

import akka.actor.{ActorRef, ActorSystem, CoordinatedShutdown}
import akka.routing.FromConfig
import com.typesafe.config.Config
import org.mbFlood.messages.MessageShutdown

trait SeedNode {
  val system : ActorSystem
  val config : Config
  val shutdownRoute: ActorRef
  def shutdown():Unit = {
    shutdownRoute ! MessageShutdown
    Thread.sleep(2000)
    CoordinatedShutdown.get(system).run(CoordinatedShutdown.unknownReason)
  }

}

object SeedNode {
  def apply(actorSystemName: String): SeedNode = new SeedNode {
    val system: ActorSystem = ActorSystem(actorSystemName)
    val shutdownRoute: ActorRef = system.actorOf(FromConfig.getInstance().props(), "shutdownRoute")
    lazy val config = system.settings.config
  }
}

