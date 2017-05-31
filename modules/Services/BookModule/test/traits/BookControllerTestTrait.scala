package traits

import author.model.AuthorMin
import book.logic.BookLogic
import book.model._
import book.traits.BookLogicTrait
import comment.model.CommentMin
import controllers.book.BookController
import editorial.model.EditorialMin
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import tests.controllers.CrudControllerTestTrait

/**
  * Created by sfrsebastian on 5/6/17.
  */
trait BookControllerTestTrait extends CrudControllerTestTrait[BookDetail, Book, BookPersistenceModel, BookTable , BookController, BookLogic] {

  var logicMock = mock[BookLogic]

  var controller = app.injector.instanceOf[BookController]

  implicit val commentMinFormat = Json.format[CommentMin]

  implicit val authorMin = Json.format[AuthorMin]

  implicit val editorialMin = Json.format[EditorialMin]

  implicit val formatDetail = Json.format[BookDetail]

  override def generatePojo: Book = factory.manufacturePojo(classOf[Book]).copy(authors = Seq(), comments = Seq())

  implicit def Model2Detail = BookDetailConverter

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[BookLogicTrait].toInstance(logicMock))
    .build
}
