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
import comment.model.CommentMin
import editorial.model.EditorialMin
import layers.controllers.ManyToManyController
import play.api.libs.json.Json

trait AuthorBookControllerTrait extends ManyToManyController[Author, AuthorPersistenceModel, AuthorTable, BookDetail, Book, BookPersistenceModel, BookTable] with AuthUserHandler {

  implicit val commentMinFormat = Json.format[CommentMin]

  implicit val authorMin = Json.format[AuthorMin]

  implicit val editorialMin = Json.format[EditorialMin]

  implicit val formatDetail = Json.format[BookDetail]

  implicit val Model2Detail = BookDetailConverter

  def relationMapper(author:Author):Seq[Book] = author.books

  def inverseRelationMapper(book:Book):Seq[Author] = book.authors
}
