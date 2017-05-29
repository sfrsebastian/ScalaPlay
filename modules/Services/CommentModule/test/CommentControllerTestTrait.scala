import book.model.{Book, BookMin}
import comment.logic.{CommentLogic, CommentLogicTrait}
import comment.model._
import controllers.comment.CommentController
import crud.tests.CrudControllerTestTrait
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/6/17.
  */
trait CommentControllerTestTrait extends CrudControllerTestTrait[CommentDetail, Comment, CommentPersistenceModel, CommentTable, CommentController, CommentLogic] {

  var logicMock = mock[CommentLogic]

  var controller = app.injector.instanceOf[CommentController]

  implicit val formatBookMin = Json.format[BookMin]

  implicit val formatDetail = Json.format[CommentDetail]

  override def generatePojo: Comment = factory.manufacturePojo(classOf[Comment]).copy(book = Book(1,"","","","",Seq(),Seq(),None))

  implicit def Model2Detail = CommentDetailConverter

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[CommentLogicTrait].toInstance(logicMock))
    .build
}