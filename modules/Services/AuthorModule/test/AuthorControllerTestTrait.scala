import author.AuthorDetail
import author.logic.{AuthorLogic, AuthorLogicTrait}
import author.model._
import book.model.{BookDTO, BookMin}
import comment.model.CommentMin
import controllers.author.AuthorController
import crud.tests.CrudControllerTestTrait
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/6/17.
  */
trait AuthorControllerTestTrait extends CrudControllerTestTrait[AuthorDetail, Author, AuthorPersistenceModel, AuthorTable, AuthorController, AuthorLogic] {

  var logicMock = mock[AuthorLogic]

  var controller = app.injector.instanceOf[AuthorController]

  implicit val formatBookMin = Json.format[BookMin]

  implicit val formatDetail = Json.format[AuthorDetail]

  override def generatePojo: Author = factory.manufacturePojo(classOf[Author]).copy(books = Seq())

  implicit def Model2Detail = AuthorDetailConverter

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[AuthorLogicTrait].toInstance(logicMock))
    .build
}
