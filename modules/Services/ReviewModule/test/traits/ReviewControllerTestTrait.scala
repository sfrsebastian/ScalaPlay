/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package traits

import book.model.{Book, BookMin}
import review.logic.ReviewLogic
import review.model._
import review.traits.ReviewLogicTrait
import controllers.Review.ReviewController
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import tests.controllers.CrudControllerTestTrait

trait ReviewControllerTestTrait extends CrudControllerTestTrait[ReviewDetail, Review, ReviewPersistenceModel, ReviewTable, ReviewController, ReviewLogic] {

  var logicMock = mock[ReviewLogic]

  var controller = app.injector.instanceOf[ReviewController]

  implicit val formatBookMin = Json.format[BookMin]

  implicit val formatDetail = Json.format[ReviewDetail]

  override def generatePojo: Review = factory.manufacturePojo(classOf[Review]).copy(book = Book(1,"","","","",Seq(),Seq(),None))

  implicit val Model2Detail = ReviewDetailConverter

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[ReviewLogicTrait].toInstance(logicMock))
    .build
}
