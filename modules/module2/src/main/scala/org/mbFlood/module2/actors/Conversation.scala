package org.mbFlood.module2.actors

import akka.actor.{Actor, Props}
import org.mbFlood.messages.{ProcessingInformation, Spread}
import org.mbFlood.module2.Node
import org.mbFlood.module2.actors.Conversation.StartConversation

class Conversation(nodeInfo: Node) extends Actor {

  def receive: Receive = {
    case StartConversation(message) =>
      nodeInfo.spreadRoute ! Spread(message,35)
    case ProcessingInformation(success, fail, _) =>
      println(s"success = $success, fail = $fail")
  }

}

object Conversation {
  case class StartConversation(message:String)
  def props(nodeInfo: Node) = Props(new Conversation(nodeInfo))
}