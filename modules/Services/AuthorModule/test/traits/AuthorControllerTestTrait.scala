/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package traits

import author.AuthorDetail
import author.logic.AuthorLogic
import author.model._
import author.traits.AuthorLogicTrait
import book.model.BookMin
import controllers.author.AuthorController
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import tests.controllers.CrudControllerTestTrait

trait AuthorControllerTestTrait extends CrudControllerTestTrait[AuthorDetail, Author, AuthorPersistenceModel, AuthorTable, AuthorController, AuthorLogic] {

  var logicMock = mock[AuthorLogic]

  var controller = app.injector.instanceOf[AuthorController]

  implicit val formatBookMin = Json.format[BookMin]

  implicit val formatDetail = Json.format[AuthorDetail]

  override def generatePojo: Author = factory.manufacturePojo(classOf[Author]).copy(books = Seq())

  implicit val Model2Detail = AuthorDetailConverter

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[AuthorLogicTrait].toInstance(logicMock))
    .build
}
