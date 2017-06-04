package traits

import book.logic.BookLogic
import book.model._
import book.traits.BookLogicTrait
import comment.logic.CommentLogic
import comment.model._
import comment.traits.CommentLogicTrait
import controllers.book.BookCommentController
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import tests.controllers.OneToManyCompositeControllerTestTrait

/**
  * Created by sfrsebastian on 5/30/17.
  */
trait BookCommentControllerTestTrait extends OneToManyCompositeControllerTestTrait[Book, BookPersistenceModel, BookTable, CommentDetail, Comment, CommentPersistenceModel, CommentTable, BookCommentController, BookLogic, CommentLogic] {

  var sourceLogicMock = mock[BookLogic]

  var destinationLogicMock = mock[CommentLogic]

  var controller = app.injector.instanceOf[BookCommentController]

  implicit val formatBookMin = Json.format[BookMin]

  implicit val formatDetail = Json.format[CommentDetail]

  implicit val Model2Detail = CommentDetailConverter

  def generatePojos(sourceId: Int, destinationId: Int): (Book, Comment) = {
    val book = factory.manufacturePojo(classOf[Book]).copy(id = sourceId, authors = Seq(), comments=Seq())
    val comment = factory.manufacturePojo(classOf[Comment]).copy(id = destinationId, book = book)
    (book.copy(comments = Seq(comment)), comment)
  }

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[BookLogicTrait].toInstance(sourceLogicMock))
    .overrides(bind[CommentLogicTrait].toInstance(destinationLogicMock))
    .build
}