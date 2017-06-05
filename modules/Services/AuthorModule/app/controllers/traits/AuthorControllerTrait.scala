/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package controllers.traits

import auth.controllers.AuthUserHandler
import author.AuthorDetail
import author.model._
import book.model.BookMin
import layers.controllers.CrudController
import play.api.libs.json.Json

trait AuthorControllerTrait extends CrudController[AuthorDetail, Author, AuthorPersistenceModel, AuthorTable] with AuthUserHandler {

  implicit val bookMinFormat=Json.format[BookMin]

  implicit val formatDetail = Json.format[AuthorDetail]

  implicit val Model2Detail = AuthorDetailConverter
}
