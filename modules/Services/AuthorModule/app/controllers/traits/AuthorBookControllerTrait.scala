/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package controllers.traits

import auth.controllers.AuthUserHandler
import author.model.{Author, AuthorMin, AuthorPersistenceModel, AuthorTable}
import book.model._
import review.model.ReviewMin
import editorial.model.EditorialMin
import layers.controllers.ManyToManyController
import play.api.libs.json.Json

trait AuthorBookControllerTrait extends ManyToManyController[Author, AuthorPersistenceModel, AuthorTable, BookDetail, Book, BookPersistenceModel, BookTable] with AuthUserHandler {

  implicit val ReviewMinFormat = Json.format[ReviewMin]

  implicit val authorMin = Json.format[AuthorMin]

  implicit val editorialMin = Json.format[EditorialMin]

  implicit val formatDetail = Json.format[BookDetail]

  implicit val Model2Detail = BookDetailConverter

  def relationMapper(author:Author):Seq[Book] = author.books

  def inverseRelationMapper(book:Book):Seq[Author] = book.authors

  override val originNotFound:String = "El autor dado no existe"

  override val destinationNotFound:String = "El libro dado no existe"

  override val destinationNotAssociated:String = "El libro dado no se encuentra asociado al autor dado"

  override val destinationAlreadyAssociated:String = "El libro dado ya se encuentra asociado al autor dado"

  override val errorAssociatingDestination:String = "Se presento un error asociando el libro con el autor"

  override val errorDisassociatingDestination:String = "Se presento un error desasociando el libro del autor"
}
