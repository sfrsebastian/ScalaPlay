import author.AuthorForm
import author.logic.{AuthorLogic, AuthorLogicTrait}
import author.model.{Author, AuthorMin, AuthorPersistenceModel, AuthorTable}
import book.model.{Book, BookMin}
import controllers.author.AuthorController
import crud.tests.CrudControllerTestTrait
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/6/17.
  */
/*trait AuthorControllerTestTrait extends CrudControllerTestTrait[AuthorForm, AuthorMin, Author, AuthorPersistenceModel, AuthorTable, AuthorController, AuthorLogic] {

  var logicMock = mock[AuthorLogic]

  var controller = app.injector.instanceOf[AuthorController]

  implicit val format = Json.format[Author]

  override def generatePojo: Author = {
    val books = for {
      _ <- 0 to 19
    }yield factory.manufacturePojo(classOf[Book])
    factory.manufacturePojo(classOf[Author]).copy(books = books)
  }

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[AuthorLogicTrait].toInstance(logicMock))
    .build
}*/
