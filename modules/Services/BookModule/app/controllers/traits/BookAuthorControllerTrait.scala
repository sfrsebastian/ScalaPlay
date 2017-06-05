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
import book.model.{Book, BookMin, BookPersistenceModel, BookTable}
import review.model.ReviewMin
import editorial.model.EditorialMin
import layers.controllers.ManyToManyController
import play.api.libs.json.Json

trait BookAuthorControllerTrait extends ManyToManyController[Book, BookPersistenceModel, BookTable, AuthorDetail, Author, AuthorPersistenceModel, AuthorTable] with AuthUserHandler{

  implicit val ReviewMinFormat = Json.format[ReviewMin]

  implicit val editorialMin = Json.format[EditorialMin]

  implicit val authorMin = Json.format[AuthorMin]

  implicit val bookMin = Json.format[BookMin]

  implicit val formatDetail = Json.format[AuthorDetail]

  implicit val Model2Detail = AuthorDetailConverter

  def relationMapper(book:Book):Seq[Author] = book.authors

  def inverseRelationMapper(author:Author):Seq[Book] = author.books
}
