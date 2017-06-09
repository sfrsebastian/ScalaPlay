/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package traits

import book.logic.BookLogic
import book.model._
import book.traits.BookLogicTrait
import review.logic.ReviewLogic
import review.model._
import review.traits.ReviewLogicTrait
import controllers.book.BookReviewController
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import tests.controllers.OneToManyCompositeControllerTestTrait

trait BookReviewControllerTestTrait extends OneToManyCompositeControllerTestTrait[Book, BookPersistenceModel, BookTable, ReviewDetail, Review, ReviewPersistenceModel, ReviewTable, BookReviewController, BookLogic, ReviewLogic] {

  var sourceLogicMock = mock[BookLogic]

  var destinationLogicMock = mock[ReviewLogic]

  var controller = app.injector.instanceOf[BookReviewController]

  implicit val formatBookMin = Json.format[BookMin]

  implicit val formatDetail = Json.format[ReviewDetail]

  implicit val Model2Detail = ReviewDetailConverter

  def generatePojos(sourceId: Int, destinationId: Int): (Book, Review) = {
    val book = factory.manufacturePojo(classOf[Book]).copy(id = sourceId, authors = Seq(), reviews=Seq())
    val Review = factory.manufacturePojo(classOf[Review]).copy(id = destinationId, book = book)
    (book.copy(reviews = Seq(Review)), Review)
  }

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[BookLogicTrait].toInstance(sourceLogicMock))
    .overrides(bind[ReviewLogicTrait].toInstance(destinationLogicMock))
    .build
}