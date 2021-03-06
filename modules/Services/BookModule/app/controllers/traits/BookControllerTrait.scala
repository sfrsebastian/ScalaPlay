/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package controllers.traits

import auth.controllers.AuthUserHandler
import author.model.AuthorMin
import book.model._
import review.model.ReviewMin
import editorial.model.EditorialMin
import layers.controllers.CrudController
import play.api.libs.json.Json

trait BookControllerTrait extends CrudController[BookDetail, Book, BookPersistenceModel, BookTable] with AuthUserHandler {

  implicit val ReviewMinFormat = Json.format[ReviewMin]

  implicit val authorMin = Json.format[AuthorMin]

  implicit val editorialMin = Json.format[EditorialMin]

  implicit val formatDetail = Json.format[BookDetail]

  implicit val Model2Detail = BookDetailConverter
}
