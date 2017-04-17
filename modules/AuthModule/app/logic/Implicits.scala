package auth.logic

import org.joda.time.DateTime
import scala.concurrent.duration._

object Implicits {
  implicit class SumDateTime(val dateTime: DateTime) extends AnyVal {
    def +(duration: Duration): DateTime = {
      dateTime.withDurationAdded(duration.toMillis, 1)
    }
  }
}