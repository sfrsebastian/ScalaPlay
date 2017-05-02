package controllers.book

import auth.controllers.AuthUserHandler
import crud.layers.CrudController
import book.logic.BookLogicTrait
import com.google.inject.Inject
import controllers.comment.CommentController
import models.book._
import play.api.libs.json.Json
import models.ModelImplicits._
import play.api.mvc.Action
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class BookController @Inject()(override val logic:BookLogicTrait, val commentController:CommentController) extends CrudController[Book, BookPersistenceModel, BookTable] with AuthUserHandler{
  override implicit val format = Json.format[Book]

  def commentDelegate(id:Int, path:String) = Action.async{request =>
    (request.method, "/" + path)match {
      case ("GET", "/") => commentController.getFromBook(id)(request)
      case _ => Future(Ok("/" + path))
    }
  }
}