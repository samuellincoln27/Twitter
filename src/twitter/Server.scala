package twitter

import akka.actor.Actor
import scala.collection.mutable.ArrayBuffer



class Server(
  numClients: Int,
  numUsers: Int,
  tweetStorage: ArrayBuffer[ArrayBuffer[String]],
  followers: ArrayBuffer[ArrayBuffer[Int]]) extends Actor {

  def receive = {
   
  case Tweet(userid, msg) =>
      //add msg to user's tweet storage list
      tweetStorage(userid) += msg
//      println("added message " + msg + " to " + userid + "'s storage")
      println(tweetStorage(3))
      
      //init userlists for each client
      var userListForClient = new ArrayBuffer[ArrayBuffer[Int]]()
      for (i <- 0 to numClients) {
        var userList = new ArrayBuffer[Int]()
        userListForClient += userList
      }

      //populate user list corresponding to each client
      for (i <- 0 to followers(userid).length - 1) {
        val follower_i = followers(userid)(i)
        val clientId = Math.floor((numClients / numUsers.toDouble) * follower_i).toInt
        userListForClient(clientId) += follower_i
      }
      
      //notify each client
      for (i <- 0 to numClients) {
        if(userListForClient(i).length != 0){
//          println("client " + i + " notifies " + userListForClient(i) + " about the message " + msg )
          context.system.actorSelection("/user/Client" + i) ! SendNotification(userListForClient(i), msg)
        }
        	
      }
      
  }
}