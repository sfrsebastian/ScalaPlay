/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package traits

import author.model._
import book.logic.EditorialBookLogic
import book.model._
import book.traits.EditorialBookLogicTrait
import review.model.ReviewMin
import controllers.editorial.EditorialBookController
import editorial.logic.EditorialLogic
import editorial.model.{Editorial, EditorialMin, EditorialPersistenceModel, EditorialTable}
import editorial.traits.EditorialLogicTrait
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import tests.controllers.OneToManyControllerTestTrait

trait EditorialBookControllerTestTrait extends OneToManyControllerTestTrait[Editorial, EditorialPersistenceModel, EditorialTable, BookDetail, Book, BookPersistenceModel, BookTable, EditorialBookController, EditorialLogic, EditorialBookLogic] {

  var sourceLogicMock = mock[EditorialLogic]

  var destinationLogicMock = mock[EditorialBookLogic]

  var controller = app.injector.instanceOf[EditorialBookController]

  implicit val ReviewMinFormat = Json.format[ReviewMin]

  implicit val authorMin = Json.format[AuthorMin]

  implicit val editorialMin = Json.format[EditorialMin]

  implicit val formatDetail = Json.format[BookDetail]

  implicit val Model2Detail = BookDetailConverter

  def generatePojos(sourceId: Int, destinationId: Int): (Editorial, Book) = {
    val editorial = factory.manufacturePojo(classOf[Editorial]).copy(id = sourceId, books = Seq())
    val book = factory.manufacturePojo(classOf[Book]).copy(id = destinationId, authors = Seq(), Reviews=Seq(), editorial = Some(editorial))
    (editorial.copy(books = Seq(book)), book)
  }

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[EditorialLogicTrait].toInstance(sourceLogicMock))
    .overrides(bind[EditorialBookLogicTrait].toInstance(destinationLogicMock))
    .build
}

