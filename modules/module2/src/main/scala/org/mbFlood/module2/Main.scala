package org.mbFlood.module2

import org.mbFlood.messages.Spread

object Main extends App {

  println("start module2")
  val node = Node("mbFlood")

  println("Enter any text or 'exit'")
  var line = "N/A"
  while (line != "exit") {
    line = scala.io.StdIn.readLine()
    // println(line)
    node.spreadRoute ! Spread(line,100)
  }

  node.shutdown()

}
