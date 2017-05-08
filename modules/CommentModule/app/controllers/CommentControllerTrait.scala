package controllers.comment

import auth.controllers.AuthUserHandler
import comment.logic.CommentLogicTrait
import comment.model._
import crud.layers.CrudController
import play.api.libs.json.Json
import play.api.mvc.Action
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sfrsebastian on 5/2/17.
  */
trait CommentControllerTrait extends CrudController[CommentForm, CommentMin, Comment, CommentPersistenceModel, CommentTable] with AuthUserHandler{

  val logic:CommentLogicTrait

  implicit val formatMin = Json.format[CommentMin]

  implicit val formatForm = Json.format[CommentForm]

  implicit def Min2Model = CommentMinConverter

  implicit def Form2Model = CommentFormConverter

  def getBookComments(bookId:Int) = Action.async {
    logic.getBookComments(bookId).map(books => Ok(Json.toJson(books.map(e=>e:CommentMin))))
  }

  def getBookComment(bookId:Int, commentId:Int) = Action.async{
    logic.get(commentId).map(c => {
      c match {
        case Some(comment) if comment.bookId == bookId => Ok(Json.toJson(comment:CommentMin))
        case _ => NotFound("No se encontró el comentario solicitado")
      }
    })
  }
}
