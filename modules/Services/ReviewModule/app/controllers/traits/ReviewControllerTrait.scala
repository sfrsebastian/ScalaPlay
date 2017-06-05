/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package controllers.traits

import auth.controllers.AuthUserHandler
import book.model.BookMin
import book.traits.BookLogicTrait
import review.model._
import review.traits.ReviewLogicTrait
import layers.controllers.CrudController
import play.api.libs.json.Json

trait ReviewControllerTrait extends CrudController[ReviewDetail, Review, ReviewPersistenceModel, ReviewTable] with AuthUserHandler{

  val logic:ReviewLogicTrait

  val bookLogic:BookLogicTrait

  implicit val formatBookMin = Json.format[BookMin]

  implicit val formatDetail = Json.format[ReviewDetail]

  implicit val Model2Detail = ReviewDetailConverter
}
