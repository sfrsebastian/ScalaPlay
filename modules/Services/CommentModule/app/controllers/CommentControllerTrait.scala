package controllers.comment

import auth.controllers.AuthUserHandler
import book.logic.BookLogicTrait
import book.model.{Book, BookMin}
import comment.logic.CommentLogicTrait
import comment.model._
import layers.controllers.CrudController
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
}
