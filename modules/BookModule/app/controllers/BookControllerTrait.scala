package controllers.book

import auth.controllers.AuthUserHandler
import author.model.AuthorMin
import book.logic.BookLogicTrait
import book.model._
import comment.model.CommentMin
import controllers.comment.CommentControllerTrait
import crud.layers.CrudController
import play.api.libs.json.Json
import play.api.mvc.Action

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sfrsebastian on 5/2/17.
  */
trait BookControllerTrait extends CrudController[BookForm, BookMin, Book, BookPersistenceModel, BookTable] with AuthUserHandler {

  val logic:BookLogicTrait
  val commentController:CommentControllerTrait

  implicit val commentMinFormat = Json.format[CommentMin]

  implicit val formatMin = Json.format[BookMin]

  implicit val formatForm = Json.format[BookForm]

  implicit def Min2Model = BookMinConverter

  implicit def Form2Model = BookFormConverter

  def getAuthorBooks(authorId:Int) = Action.async {
    logic.getAuthorBooks(authorId).map(books => Ok(Json.toJson(books.map(e=>e:BookMin))))
  }

  def getAuthorBook(authorId:Int, bookId:Int) = Action.async{
    logic.get(bookId).map(b => {
      b match {
        case Some(book) if book.authors.map(_.id).contains(authorId) => Ok(Json.toJson(book:BookMin))
        case _ => NotFound("No se encontró el libro solicitado")
      }
    })
  }

  def getEditorialBooks(editorialId:Int) = Action.async {
    logic.getEditorialBooks(editorialId).map(books => Ok(Json.toJson(books.map(e=>e:BookMin))))
  }

  def getEditorialBook(editorialId:Int, bookId:Int) = Action.async{
    logic.get(bookId).map(b => {
      b match {
        case Some(book) if book.editorial.map(_.id == editorialId).getOrElse(false) => Ok(Json.toJson(book:BookMin))
        case _ => NotFound("No se encontró el libro solicitado")
      }
    })
  }
}
