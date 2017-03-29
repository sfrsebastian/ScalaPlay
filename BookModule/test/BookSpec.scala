import logic.bookModule.BookLogic
import models.bookModule.Book
import org.scalatestplus.play._
import play.api.mvc.{Result, Results}
import play.api.test.FakeRequest

import scala.concurrent.Future

class BookSpec extends PlaySpec with Results {

  "BookController" should {
    "return collection" in {
      val result: Future[Seq[Book]] = BookLogic.getAll
      assert(result.value.size>=0)
    }
  }
}