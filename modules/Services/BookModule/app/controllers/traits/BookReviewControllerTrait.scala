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

  def relationMapper(book:Book):Seq[Review] = book.reviews

  def aggregationMapper(destination: Review, source: Book): Review = destination.copy(book = source)

  override val originNotFound:String = "El libro dado no existe"

  override val destinationNotFound:String = "El comentario solicitado no existe"

  override val destinationNotAssociated:String = "El comentario no se encuentra asociado al libro dado"

  override val errorCreatingDestination:String = "Se presento un error creando el comentario en el libro"

  override val errorUpdatingDestination:String = "Se presento un error actualizando el el comentario"

  override val errorDeletingDestination:String = "Se presento un error eliminando el comentario del libro"
}
