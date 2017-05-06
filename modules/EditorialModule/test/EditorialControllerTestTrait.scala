import controllers.editorial.EditorialController
import crud.tests.CrudControllerTestTrait
import editorial.logic.{EditorialLogic, EditorialLogicTrait}
import editorial.model.{Editorial, EditorialPersistenceModel, EditorialTable}
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/6/17.
  */
trait EditorialControllerTestTrait extends CrudControllerTestTrait[Editorial, EditorialPersistenceModel, EditorialTable, EditorialController, EditorialLogic] {

  var logicMock = mock[EditorialLogic]

  var controller = app.injector.instanceOf[EditorialController]

  implicit val format = Json.format[Editorial]

  override def generatePojo: Editorial = factory.manufacturePojo(classOf[Editorial])

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[EditorialLogicTrait].toInstance(logicMock))
    .build
}
