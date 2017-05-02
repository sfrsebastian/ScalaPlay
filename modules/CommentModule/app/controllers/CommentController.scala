package controllers.comment

import comment.logic.CommentLogicTrait
import models.comment._
import com.google.inject.Inject
import crud.layers.CrudController
import play.api.libs.json.Json
import auth.controllers.AuthUserHandler
import play.api.mvc.Action
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sfrsebastian on 4/26/17.
  */
class CommentController @Inject()(override val logic:CommentLogicTrait) extends CrudController[Comment, CommentPersistenceModel, CommentTable] with AuthUserHandler {
  override implicit val format = Json.format[Comment]

  def getFromBook(id:Int) = Action.async {
    logic.getFromBook(id).map(elements => Ok(Json.toJson(elements)))
  }
}