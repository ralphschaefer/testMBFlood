package org.mbFlood.module1.actors

import akka.actor.{Actor, DeadLetter, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.MemberEvent
import akka.routing.FromConfig
import org.mbFlood.messages.{ProcessingInformation, SimpleMessage, Spread}

class SpreadMessage extends Actor {

  import SpreadMessage._

  private val refMapper = collection.mutable.HashMap[Int,ProcessingInformation]()

  private def mapperRegisterFailAndComplete(item:SimpleMessage) = {
    refMapper.get(item.messageRef).map( i =>
      i.copy(unprocessed = i.unprocessed -1, fail = i.fail + 1)
    ).foreach( i =>
    refMapper(item.messageRef)=i
    )
    if (testAllProcessed(item.messageRef))
      item.spreadOrigin ! refMapper(item.messageRef)
  }

  private def mapperRegisterSuccessAndComplete(item:SimpleMessage) = {
    refMapper.get(item.messageRef).map(i =>
      i.copy(unprocessed = i.unprocessed - 1, success = i.success + 1)
    ).foreach(i =>
      refMapper(item.messageRef) = i
    )
    if (testAllProcessed(item.messageRef))
      item.spreadOrigin ! refMapper(item.messageRef)
  }

  val cluster = Cluster(context.system)

  override def preStart(): Unit = {
    cluster.subscribe(self, classOf[MemberEvent])
    context.system.eventStream.subscribe(self,  classOf[DeadLetter])
  }

  override def postStop(): Unit = cluster.unsubscribe(self)

  val consumerRoute = context.system.actorOf(FromConfig.getInstance().props(), "consumerRoute")

  private def testAllProcessed(ref : Int):Boolean =
    refMapper.get(ref).forall(_.unprocessed == 0)

  def receive: Receive = {
    case Spread(msg,factor) =>
      // send message many times to consumer
      println(s"spread $msg $factor times")
      val ref = getRef
      refMapper(ref)=new ProcessingInformation(factor)
      (1 to factor).foreach( i => consumerRoute ! SimpleMessage(s"$msg: $i", sender(), ref) )
    case m:MemberEvent =>
      println("member Event: " + m)
    case DeadLetter(message:SimpleMessage, sender, recipient) =>
      sender ! Unprocessable(message)
    case Unprocessable(item) =>
      println("not processed message :" + item)
      mapperRegisterFailAndComplete(item)
    case Akn(item:SimpleMessage) =>
      println("processed item : " + item)
      mapperRegisterSuccessAndComplete(item)
  }
}

object SpreadMessage {

  case class Akn(item:SimpleMessage)

  case class Unprocessable(item:SimpleMessage)

  def props = Props(new SpreadMessage)

  private var refCounter : Int = 1

  def getRef : Int = {
    refCounter += 1
    refCounter
  }

}

