import auth.controllers.BookController
import common.traits.test.CrudControllerTestTrait
import logic.bookModule.BookLogic
import models.bookModule.{Book, Books}
import play.api.libs.json.Json

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
