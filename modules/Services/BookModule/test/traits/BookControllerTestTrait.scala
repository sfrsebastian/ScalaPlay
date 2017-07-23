/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package traits

import author.model.AuthorMin
import book.logic.BookLogic
import book.model._
import book.traits.BookLogicTrait
import review.model.ReviewMin
import controllers.book.BookController
import editorial.model.EditorialMin
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import tests.controllers.CrudControllerTestTrait

trait BookControllerTestTrait extends CrudControllerTestTrait[BookDetail, Book, BookPersistenceModel, BookTable , BookController, BookLogic] {

  var logicMock = mock[BookLogic]

  var controller = app.injector.instanceOf[BookController]

  implicit val ReviewMinFormat = Json.format[ReviewMin]

  implicit val authorMin = Json.format[AuthorMin]

  implicit val editorialMin = Json.format[EditorialMin]

  implicit val formatDetail = Json.format[BookDetail]

  override def generatePojo: Book = factory.manufacturePojo(classOf[Book]).copy(authors = Seq(), reviews = Seq())

  implicit val Model2Detail = BookDetailConverter

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[BookLogicTrait].toInstance(logicMock))
    .build
}
