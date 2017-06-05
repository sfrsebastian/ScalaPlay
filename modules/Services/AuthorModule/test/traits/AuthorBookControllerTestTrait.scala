/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package traits

import author.logic.AuthorLogic
import author.model.{Author, AuthorMin, AuthorPersistenceModel, AuthorTable}
import author.traits.AuthorLogicTrait
import book.logic.AuthorBookLogic
import book.model._
import book.traits.AuthorBookLogicTrait
import review.model.ReviewMin
import controllers.author.AuthorBookController
import editorial.model.EditorialMin
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import tests.controllers.ManyToManyControllerTestTrait

trait AuthorBookControllerTestTrait extends ManyToManyControllerTestTrait[Author, AuthorPersistenceModel, AuthorTable, BookDetail, Book, BookPersistenceModel, BookTable, AuthorBookController, AuthorLogic, AuthorBookLogic] {

  var sourceLogicMock = mock[AuthorLogic]

  var destinationLogicMock = mock[AuthorBookLogic]

  var controller = app.injector.instanceOf[AuthorBookController]

  implicit val ReviewMinFormat = Json.format[ReviewMin]

  implicit val authorMin = Json.format[AuthorMin]

  implicit val editorialMin = Json.format[EditorialMin]

  implicit val formatDetail = Json.format[BookDetail]

  implicit val Model2Detail = BookDetailConverter

  def generatePojos(sourceId: Int, destinationId: Int): (Author, Book) = {
    val author = factory.manufacturePojo(classOf[Author]).copy(id = sourceId, books = Seq())
    val book = factory.manufacturePojo(classOf[Book]).copy(id = destinationId, authors = Seq(author), Reviews = Seq())
    (author.copy(books = Seq(book)), book)
  }

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[AuthorLogicTrait].toInstance(sourceLogicMock))
    .overrides(bind[AuthorBookLogicTrait].toInstance(destinationLogicMock))
    .build
}
