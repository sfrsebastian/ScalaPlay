package controllers.book

import auth.controllers.AuthUserHandler
import author.model.AuthorMin
import book.model._
import comment.model.CommentMin
import editorial.model.EditorialMin
import layers.controllers.CrudController
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/2/17.
  */
trait BookControllerTrait extends CrudController[BookDetail, Book, BookPersistenceModel, BookTable] with AuthUserHandler {

  implicit val commentMinFormat = Json.format[CommentMin]

  implicit val authorMin = Json.format[AuthorMin]

  implicit val editorialMin = Json.format[EditorialMin]

  implicit val formatDetail = Json.format[BookDetail]

  implicit def Detail2Model = BookDetailConverter
}
