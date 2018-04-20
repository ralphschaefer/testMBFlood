package org.mbFlood.messages

import akka.actor.ActorRef

case class SimpleMessage (message: String, spreadOrigin: ActorRef, messageRef:Int) {
  override def toString: String = s"MSG($message)"
}
