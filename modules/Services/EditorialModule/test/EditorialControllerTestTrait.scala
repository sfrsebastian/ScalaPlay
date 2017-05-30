import author.model.AuthorMin
import book.model.{BookDetail, BookMin}
import comment.model.CommentMin
import controllers.editorial.EditorialController
import crud.tests.CrudControllerTestTrait
import editorial.logic.EditorialLogic
import editorial.model._
import editorial.traits.EditorialLogicTrait
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/6/17.
  */
trait EditorialControllerTestTrait extends CrudControllerTestTrait[EditorialDetail, Editorial, EditorialPersistenceModel, EditorialTable, EditorialController, EditorialLogic] {

  var logicMock = mock[EditorialLogic]

  var controller = app.injector.instanceOf[EditorialController]

  implicit val authorMin = Json.format[AuthorMin]

  implicit val editorialMin = Json.format[BookMin]

  implicit val formatDetail = Json.format[EditorialDetail]

  override def generatePojo: Editorial = factory.manufacturePojo(classOf[Editorial]).copy(books = Seq())

  implicit def Model2Detail = EditorialDetailConverter

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[EditorialLogicTrait].toInstance(logicMock))
    .build
}
