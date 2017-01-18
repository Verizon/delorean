package delorean

import scalaz.\/
import scalaz.concurrent.Task

object compatibility {
  implicit final class BedazledTask[A](val task: Task[A]) extends AnyVal {
    def unsafePerformAsync(g: (Throwable \/ A) => Unit): Unit = task.runAsync(g)
    def unsafePerformSync: A = task.run
  }
}
