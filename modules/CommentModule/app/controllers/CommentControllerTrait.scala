package controllers.comment

import auth.controllers.AuthUserHandler
import author.model.AuthorMin
import book.model.BookMin
import comment.logic.CommentLogicTrait
import comment.model._
import crud.layers.CrudController
import editorial.model.EditorialMin
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

  def getFromBook(id:Int) = Action.async {
    logic.getFromBook(id).map(elements => Ok(Json.toJson(elements.map(e=>e:CommentMin))))
  }
}
