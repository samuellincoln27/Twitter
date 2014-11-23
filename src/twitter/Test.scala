package twitter

import akka.actor.Actor
import scala.collection.mutable.ArrayBuffer
import scala.util.Random



class Test(testId : Int) extends Actor{
	
  
  def receive = {
    case SendNotification(userList, msg) =>
      val rand = new Random()
      val notifyUser = rand.nextInt(userList.length)
      val serverid = rand.nextInt(10)
      context.system.actorSelection("/user/Server" + serverid) ! Tweet(notifyUser, "TF"+testId+"TS"+serverid)
  }
}