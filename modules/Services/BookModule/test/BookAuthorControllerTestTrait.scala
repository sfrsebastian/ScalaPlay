import author.model._
import author.traits.BookAuthorLogicTrait
import author.{AuthorDetail, BookAuthorLogic}
import book.logic.BookLogic
import book.model._
import book.traits.BookLogicTrait
import comment.model.CommentMin
import controllers.book.BookAuthorController
import editorial.model.EditorialMin
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import tests.controllers.ManyToManyControllerTestTrait

/**
  * Created by sfrsebastian on 5/29/17.
  */
trait BookAuthorControllerTestTrait extends ManyToManyControllerTestTrait[Book, BookPersistenceModel, BookTable, AuthorDetail, Author, AuthorPersistenceModel, AuthorTable, BookAuthorController, BookLogic, BookAuthorLogic] {

  var sourceLogicMock = mock[BookLogic]

  var destinationLogicMock = mock[BookAuthorLogic]

  var controller = app.injector.instanceOf[BookAuthorController]

  implicit val commentMinFormat = Json.format[CommentMin]

  implicit val editorialMin = Json.format[EditorialMin]

  implicit val authorMin = Json.format[AuthorMin]

  implicit val bookMin = Json.format[BookMin]

  implicit val formatDetail = Json.format[AuthorDetail]

  implicit def Model2Detail = AuthorDetailConverter

  def generatePojos(sourceId: Int, destinationId: Int): (Book, Author) = {
    val book = factory.manufacturePojo(classOf[Book]).copy(id = sourceId, authors = Seq(), comments = Seq())
    val author = factory.manufacturePojo(classOf[Author]).copy(id = destinationId, books = Seq(book))
    (book.copy(authors = Seq(author)), author)
  }

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[BookLogicTrait].toInstance(sourceLogicMock))
    .overrides(bind[BookAuthorLogicTrait].toInstance(destinationLogicMock))
    .build
}
