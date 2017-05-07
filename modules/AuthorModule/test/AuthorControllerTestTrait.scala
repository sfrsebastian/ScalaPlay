import author.AuthorForm
import author.logic.{AuthorLogic, AuthorLogicTrait}
import author.model._
import book.model.BookMin
import comment.model.CommentMin
import controllers.author.AuthorController
import crud.tests.CrudControllerTestTrait
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/6/17.
  */
trait AuthorControllerTestTrait extends CrudControllerTestTrait[AuthorForm, AuthorMin, Author, AuthorPersistenceModel, AuthorTable, AuthorController, AuthorLogic] {

  var logicMock = mock[AuthorLogic]

  var controller = app.injector.instanceOf[AuthorController]

  implicit val formatCommentMin = Json.format[CommentMin]

  implicit val formatBookMin = Json.format[BookMin]

  implicit val formatMin = Json.format[AuthorMin]

  implicit val formatForm = Json.format[AuthorForm]

  override def generatePojo: Author = factory.manufacturePojo(classOf[Author]).copy(books = Seq())

  implicit def Model2Form = AuthorFormConverter

  implicit def Model2Min = AuthorMinConverter

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[AuthorLogicTrait].toInstance(logicMock))
    .build
}
