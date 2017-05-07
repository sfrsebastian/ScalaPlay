import comment.logic.{CommentLogic, CommentLogicTrait}
import comment.model.{Comment, CommentPersistenceModel, CommentTable}
import controllers.comment.CommentController
import crud.tests.CrudControllerTestTrait
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/6/17.
  */
/*trait CommentControllerTestTrait extends CrudControllerTestTrait[Comment, CommentPersistenceModel, CommentTable, CommentController, CommentLogic] {

  var logicMock = mock[CommentLogic]

  var controller = app.injector.instanceOf[CommentController]

  implicit val format = Json.format[Comment]

  override def generatePojo: Comment = factory.manufacturePojo(classOf[Comment])

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[CommentLogicTrait].toInstance(logicMock))
    .build
}*/
