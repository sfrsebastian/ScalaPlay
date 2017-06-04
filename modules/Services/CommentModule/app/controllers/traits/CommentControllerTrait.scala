package controllers.traits

import auth.controllers.AuthUserHandler
import book.model.BookMin
import book.traits.BookLogicTrait
import comment.model._
import comment.traits.CommentLogicTrait
import layers.controllers.CrudController
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/2/17.
  */
trait CommentControllerTrait extends CrudController[CommentDetail, Comment, CommentPersistenceModel, CommentTable] with AuthUserHandler{

  val logic:CommentLogicTrait

  val bookLogic:BookLogicTrait

  implicit val formatBookMin = Json.format[BookMin]

  implicit val formatDetail = Json.format[CommentDetail]

  implicit val Model2Detail = CommentDetailConverter
}
