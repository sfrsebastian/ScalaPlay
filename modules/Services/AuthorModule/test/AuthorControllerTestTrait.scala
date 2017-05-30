import author.AuthorDetail
import author.logic.AuthorLogic
import author.model._
import author.traits.AuthorLogicTrait
import book.model.{BookDTO, BookMin}
import comment.model.CommentMin
import controllers.author.AuthorController
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import tests.controllers.CrudControllerTestTrait

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
