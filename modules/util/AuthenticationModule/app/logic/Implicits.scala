/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package auth.logic

import org.joda.time.DateTime
import scala.concurrent.duration._

/**
  * Implicito de manejo de tiempo
  */
object Implicits {
  implicit class SumDateTime(val dateTime: DateTime) extends AnyVal {
    def +(duration: FiniteDuration): DateTime = {
      dateTime.withDurationAdded(duration.toMillis, 1)
    }
  }
}