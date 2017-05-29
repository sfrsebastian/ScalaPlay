package controllers.book

import auth.controllers.AuthUserHandler
import author.logic.AuthorLogicTrait
import author.model.AuthorMin
import book.logic.BookLogicTrait
import book.model._
import comment.model.CommentMin
import crud.layers.CrudController
import editorial.model.EditorialMin
import play.api.libs.json.Json
import play.api.mvc.Action
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sfrsebastian on 5/2/17.
  */
trait BookControllerTrait extends CrudController[BookDetail, Book, BookPersistenceModel, BookTable] with AuthUserHandler {

  val logic:BookLogicTrait
  val authorLogic:AuthorLogicTrait

  implicit val commentMinFormat = Json.format[CommentMin]

  implicit val authorMin = Json.format[AuthorMin]

  implicit val editorialMin = Json.format[EditorialMin]

  implicit val formatDetail = Json.format[BookDetail]

  implicit def Detail2Model = BookDetailConverter
}
