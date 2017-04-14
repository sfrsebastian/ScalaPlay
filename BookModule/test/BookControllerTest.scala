import akka.stream.Materializer
import common.traits.app.CrudLogic
import common.traits.test.CrudControllerTestTrait
import controllers.bookModule.BookController
import logic.bookModule.BookLogic
import models.bookModule.{Book, Books}
import org.mockito.Mockito.when
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.FakeRequest
import play.api.test.Helpers._
import org.mockito.ArgumentMatchers._
import play.api.libs.json.{Format, Json}
import uk.co.jemos.podam.api.PodamFactoryImpl

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

/**
  * Created by sfrsebastian on 4/12/17.
  */
class BookControllerTest extends CrudControllerTestTrait[Book, Books, BookController, BookLogic] {

  var logicMock = mock[BookLogic]
  var controller = new BookController(logicMock)

  implicit val format = Json.format[Book]

  override def generatePojo: Book = factory.manufacturePojo(classOf[Book])

  override def beforeEach(){
    logicMock = mock[BookLogic]
    controller = new BookController(logicMock)
  }
}
