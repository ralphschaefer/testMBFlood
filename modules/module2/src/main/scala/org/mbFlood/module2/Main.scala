package org.mbFlood.module2

import org.mbFlood.module2.actors.Conversation

object Main extends App {

  println("start module2")
  val node = Node("mbFlood")

  val converstaion = node.system.actorOf(Conversation.props(node))

  println("Enter any text or 'exit'")
  var line = "N/A"
  while (line != "exit") {
    line = scala.io.StdIn.readLine()
    converstaion ! Conversation.StartConversation(line)
  }

  node.shutdown()

}
