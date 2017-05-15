package controllers.comment

import auth.controllers.AuthUserHandler
import book.logic.BookLogicTrait
import book.model.{Book, BookMin}
import comment.logic.CommentLogicTrait
import comment.model._
import crud.layers.CrudController
import play.api.libs.json.Json
import play.api.mvc.{Action, Result}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 5/2/17.
  */
trait CommentControllerTrait extends CrudController[CommentDetail, Comment, CommentPersistenceModel, CommentTable] with AuthUserHandler{

  val logic:CommentLogicTrait

  val bookLogic:BookLogicTrait

  implicit val formatBookMin = Json.format[BookMin]

  implicit val formatDetail = Json.format[CommentDetail]

  implicit def Detail2Model = CommentDetailConverter

  def getBookComments(bookId:Int, start:Option[Int], limit:Option[Int]) = Action.async {
    for{
      book <- bookLogic.get(bookId)
      comments <- logic.getAll(start.getOrElse(0), limit.getOrElse(Int.MaxValue), bookId)
    }yield validateBook(book, Ok(Json.toJson(comments.map(e => e: CommentDetail))))
  }

  def getBookComment(bookId:Int, commentId:Int) = Action.async{
    for{
      book <- bookLogic.get(bookId)
      comment <- logic.get(bookId, commentId)
    }yield validateBook(book, validateComment(comment))
  }

  def validateBook(book:Option[Book], f: Result) = {
    book match {
      case Some(_) => f
      case None => NotFound("No se encontró el libro solicitado")
    }
  }

  def validateComment(comment:Option[Comment]) = {
    comment match {
      case Some(c) => Ok(Json.toJson(c:CommentDetail))
      case None => NotFound("No se encontró el comentario solicitado")
    }
  }
}
