package inf

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success}

import scalaz.{-\/, \/-}
import scalaz.concurrent.{Strategy, Task}

package object delorean {

  implicit class FutureAPI[A](self: => Future[A]) {

    def toTask(implicit ec: ExecutionContext, S: Strategy): Task[A] = {
      Task async { cb =>
        self onComplete {
          case Success(a) => S { cb(\/-(a)) }
          case Failure(t) => S { cb(-\/(t)) }
        }
      }
    }
  }

  implicit class TaskAPI[A](val self: Task[A]) extends AnyVal {

    def unsafeToFuture(): Future[A] = {
      val p = Promise[A]()

      self runAsync {
        case \/-(a) => p.complete(Success(a)); ()
        case -\/(t) => p.complete(Failure(t)); ()
      }

      p.future
    }
  }
}