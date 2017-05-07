package controllers.book

import auth.controllers.AuthUserHandler
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

  def commentDelegate(id:Int, path:String) = Action.async{request =>
    (request.method, "/" + path)match {
      case ("GET", "/") => commentController.getFromBook(id)(request)
      case _ => Future(Ok("/" + path))
    }
  }
}
