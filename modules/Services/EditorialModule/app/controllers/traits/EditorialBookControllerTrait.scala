package controllers.traits

import auth.controllers.AuthUserHandler
import author.model.AuthorMin
import book.model._
import comment.model.CommentMin
import editorial.model.{Editorial, EditorialMin, EditorialPersistenceModel, EditorialTable}
import layers.controllers.OneToManyController
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/29/17.
  */
trait EditorialBookControllerTrait extends OneToManyController[Editorial, EditorialPersistenceModel, EditorialTable, BookDetail, Book, BookPersistenceModel, BookTable] with AuthUserHandler{
  implicit val commentMinFormat = Json.format[CommentMin]

  implicit val authorMin = Json.format[AuthorMin]

  implicit val editorialMin = Json.format[EditorialMin]

  implicit val formatDetail = Json.format[BookDetail]

  implicit def Detail2Model = BookDetailConverter

  def relationMapper(editorial:Editorial):Seq[Book] = editorial.books
}
