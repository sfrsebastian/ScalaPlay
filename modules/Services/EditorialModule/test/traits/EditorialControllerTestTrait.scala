/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package traits

import author.model.AuthorMin
import book.model.BookMin
import controllers.editorial.EditorialController
import editorial.logic.EditorialLogic
import editorial.model._
import editorial.traits.EditorialLogicTrait
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import tests.controllers.CrudControllerTestTrait

trait EditorialControllerTestTrait extends CrudControllerTestTrait[EditorialDetail, Editorial, EditorialPersistenceModel, EditorialTable, EditorialController, EditorialLogic] {

  var logicMock = mock[EditorialLogic]

  var controller = app.injector.instanceOf[EditorialController]

  implicit val authorMin = Json.format[AuthorMin]

  implicit val editorialMin = Json.format[BookMin]

  implicit val formatDetail = Json.format[EditorialDetail]

  override def generatePojo: Editorial = factory.manufacturePojo(classOf[Editorial]).copy(books = Seq())

  implicit val Model2Detail = EditorialDetailConverter

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[EditorialLogicTrait].toInstance(logicMock))
    .build
}
