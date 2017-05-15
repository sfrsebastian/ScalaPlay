import book.logic.{BookLogic, BookLogicTrait}
import book.model._
import comment.model.CommentMin
import controllers.book.BookController
import crud.tests.CrudControllerTestTrait
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/6/17.
  */
trait BookControllerTestTrait extends CrudControllerTestTrait[BookDetail, BookDTO, Book, BookPersistenceModel, BookTable , BookController, BookLogic] {

  var logicMock = mock[BookLogic]

  var controller = app.injector.instanceOf[BookController]

  implicit val formatCommentMin = Json.format[CommentMin]

  implicit val formatMin = Json.format[BookDTO]

  implicit val formatForm = Json.format[BookDetail]

  override def generatePojo: Book = factory.manufacturePojo(classOf[Book]).copy(authors = Seq(), comments = Seq())

  implicit def Model2Form = BookDetailDTOConverter

  implicit def Model2Min = BookDTOConverter

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[BookLogicTrait].toInstance(logicMock))
    .build
}
