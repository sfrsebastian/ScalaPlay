package controllers.comment

import auth.controllers.AuthUserHandler
import book.model.BookMin
import comment.logic.CommentLogicTrait
import comment.model.{Comment, CommentPersistenceModel, CommentTable}
import crud.layers.CrudController
import play.api.libs.json.Json
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sfrsebastian on 5/2/17.
  */
trait CommentControllerTrait extends CrudController[Comment, CommentPersistenceModel, CommentTable] with AuthUserHandler{

  val logic:CommentLogicTrait

  implicit val formatBook = Json.format[BookMin]
  override implicit val format = Json.format[Comment]

  def getFromBook(id:Int) = Action.async {
    logic.getFromBook(id).map(elements => Ok(Json.toJson(elements)))
  }
}
