package org.mbFlood.messages

case class ProcessingInformation(success:Int, fail: Int, unprocessed: Int) {
  def this(unprocessed: Int) = this(0,0,unprocessed)
}