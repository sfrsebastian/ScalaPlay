import book.logic.{BookLogic, BookLogicTrait}
import book.model.{Book, BookPersistenceModel, BookTable}
import comment.model.CommentMin
import controllers.book.BookController
import crud.tests.CrudControllerTestTrait
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/6/17.
  */
/*trait BookControllerTestTrait extends CrudControllerTestTrait[Book, BookPersistenceModel, BookTable , BookController, BookLogic] {

  var logicMock = mock[BookLogic]

  var controller = app.injector.instanceOf[BookController]

  implicit val format = Json.format[Book]

  override def generatePojo: Book = {
    val comments = for {
      _ <- 0 to 19
    }yield factory.manufacturePojo(classOf[CommentMin])
    factory.manufacturePojo(classOf[Book]).copy(comments = comments)
  }

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[BookLogicTrait].toInstance(logicMock))
    .build
}*/
