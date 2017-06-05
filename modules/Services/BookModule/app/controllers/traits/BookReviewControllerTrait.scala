/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package controllers.traits

import auth.controllers.AuthUserHandler
import book.model.{Book, BookMin, BookPersistenceModel, BookTable}
import review.model._
import layers.controllers.OneToManyCompositeController
import play.api.libs.json.Json

trait BookReviewControllerTrait extends OneToManyCompositeController[Book, BookPersistenceModel, BookTable, ReviewDetail, Review, ReviewPersistenceModel, ReviewTable] with AuthUserHandler{

  implicit val formatBookMin = Json.format[BookMin]

  implicit val formatDetail = Json.format[ReviewDetail]

  implicit val Model2Detail = ReviewDetailConverter

  def relationMapper(book:Book):Seq[Review] = book.Reviews

  def aggregationMapper(destination: Review, source: Book): Review = destination.copy(book = source)
}
