package twitter

import akka.actor.Actor
import scala.collection.mutable.ArrayBuffer
import scala.util.Random



class Client(testId : Int,numUsers: Int) extends Actor{
	
  
  def receive = {
    case SendNotification(userList, msg) =>
      val rand = new Random()
      val startTime = System.currentTimeMillis()
      val notifyUser = rand.nextInt(userList.length)
      val serverid = rand.nextInt(10)
      //wait time determined on which bucket the user belongs to
      val bucket = (testId*6)/numUsers
      if(bucket==0){
        while(System.currentTimeMillis()-startTime <1000){}}
      else if(bucket==1){
        while(System.currentTimeMillis()-startTime <10000){}}
      else if(bucket == 2){
       while(System.currentTimeMillis()-startTime <20000){}
      }else if(bucket == 3){
        while(System.currentTimeMillis()-startTime <60000){}
      }
      if(bucket<=3)
    	  context.system.actorSelection("/user/Server" + serverid) ! Tweet(notifyUser, "TF"+testId+"TS"+serverid)
  }
}