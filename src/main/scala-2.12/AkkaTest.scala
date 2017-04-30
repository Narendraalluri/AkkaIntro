import akka.actor.{Actor, ActorRef, ActorSystem, Inbox, Props}
import akka.pattern.ask
import akka.util.Timeout
import akka.pattern.{ask, pipe}
import scala.concurrent.duration._
import akka.util.Timeout
import scala.concurrent.duration.Duration

// #message_snippet
case object Greet
case class WhoToGreet(who: String)
case class Greeting(message: String)
// end #message_snippet

// #actor_snippet
class Greeter extends Actor {

  def receive = {
      case WhoToGreet(who) => context.actorOf(Props[Greeter1]) ! Greeting(s"hello, $who")
      case Greeting(msg) => println(msg)
  }
}

class Greeter1 extends Actor {

  def receive = {
    case Greeting(msg) => sender ! Greeting(msg)
  }
}
// end #actor_snippet

object HelloAkkaScala extends App {
  // #create_snippet
  // Create the 'helloakka' actor system
  val system = ActorSystem("helloakka")
  implicit val timeout: Timeout = 10 seconds


  // Create the 'greeter' actor
  val greeter = system.actorOf(Props[Greeter], "greeter")
  val greeter1 = system.actorOf(Props[Greeter1], "greeter1")
  // end #create_snippet
  greeter ! WhoToGreet("akka")
  greeter1 tell (Greeting("akka"), greeter)


}
