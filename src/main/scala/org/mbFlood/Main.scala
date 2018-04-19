package org.mbFlood

object Main extends App {
    
  println("Startup mbFlood Seed")

  val seedNode = SeedNode("mbFlood")
  println("ANY Key?")
  scala.io.StdIn.readLine()
  seedNode.shutdown()


}