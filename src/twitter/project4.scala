package twitter

import akka.actor.ActorSystem
import akka.actor.Props
import scala.collection.mutable.ArrayBuffer
import scala.util.Random


case class SendNotification(userList: ArrayBuffer[Int], msg : String)
case class Tweet(userid: Int, msg: String)



object project4 {
  def main(args: Array[String]) {
    twitter(numServers = 10, numClients = 10, numUsers = 40);
  }

  def twitter(numServers: Int, numClients: Int, numUsers: Int) {
    val system = ActorSystem("twitter")

    //initialize buffers for tweet storage and follower list for each user
    var tweetStorage = new ArrayBuffer[ ArrayBuffer[String] ]()
    var followers = new ArrayBuffer[ ArrayBuffer[Int] ]()
    
    
    for(i <- 0 to numUsers) {
      val tweetArray = new ArrayBuffer[String]
      tweetStorage += tweetArray
      
      //choose a random no for the no of followers for this user
      val follArray = new ArrayBuffer[Int]
      val rand = new Random()
      val numFollowers = rand.nextInt(numUsers);
      
      //randomly select corresponding no of followers based on the above numFollowers
      for(users <- 0 to numFollowers){
    	var nextFollower = rand.nextInt(numUsers);
    	while(nextFollower == i){
    	  nextFollower = rand.nextInt(numUsers);
    	}

        follArray += nextFollower
      }
      followers += follArray
    }
    
    
    //activate server actors
    for (i <- 0 to numServers) {
    	val server_i = system.actorOf(Props(new Server(numServers, numUsers, tweetStorage, followers)), "Server" + i)
    }
    
    //activate client actors
    for (i <- 0 to numClients) {
    	val client_i = system.actorOf(Props(new Client(i,numUsers)), "Client" + i)
    }
    
    //first tweet
    system.actorSelection("/user/Server" + 0) ! Tweet(3 , "message by 3")
  }
}