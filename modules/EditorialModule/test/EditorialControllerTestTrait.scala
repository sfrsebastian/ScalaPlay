import book.model.BookMin
import comment.model.CommentMin
import controllers.editorial.EditorialController
import crud.tests.CrudControllerTestTrait
import editorial.logic.{EditorialLogic, EditorialLogicTrait}
import editorial.model._
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/6/17.
  */
trait EditorialControllerTestTrait extends CrudControllerTestTrait[EditorialForm, EditorialMin, Editorial, EditorialPersistenceModel, EditorialTable, EditorialController, EditorialLogic] {

  var logicMock = mock[EditorialLogic]

  var controller = app.injector.instanceOf[EditorialController]

  implicit val formatCommentMin = Json.format[CommentMin]

  implicit val formatBookMin = Json.format[BookMin]

  implicit val formatMin = Json.format[EditorialMin]

  implicit val formatForm = Json.format[EditorialForm]

  override def generatePojo: Editorial = factory.manufacturePojo(classOf[Editorial]).copy(books = Seq())

  implicit def Model2Form = EditorialFormConverter

  implicit def Model2Min = EditorialMinConverter

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[EditorialLogicTrait].toInstance(logicMock))
    .build
}
