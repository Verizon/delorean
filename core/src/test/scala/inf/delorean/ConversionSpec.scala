package inf.delorean

import scala.concurrent._
import scala.concurrent.duration._

import scalaz.concurrent.Task

import org.scalatest._
import org.scalatest.prop._
import org.scalacheck._

class ConversionSpec extends FlatSpec with Matchers with PropertyChecks {
  import Arbitrary._

  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

  behavior of "task-future conversions"

  it should "convert a future to a task that produces the same value" in {
    forAll { str: String =>
      val f = Future(str)
      val t = f.toTask

      t.run shouldEqual str
    }
  }

  it should "convert a task to a future that produces the same value" in {
    forAll { str: String =>
      val t = Task now str
      val f = t.unsafeToFuture

      Await.result(f, Duration.Inf) shouldEqual str
    }
  }
}