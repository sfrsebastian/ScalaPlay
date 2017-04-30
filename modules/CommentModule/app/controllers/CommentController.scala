package controllers.comment

import comment.logic.CommentLogicTrait
import comment.models.{Comment, Comments}
import com.google.inject.Inject
import crud.layers.CrudController
import play.api.libs.json.Json
import auth.controllers.AuthUserHandler

/**
  * Created by sfrsebastian on 4/26/17.
  */
class CommentController @Inject()(override val logic:CommentLogicTrait) extends CrudController[Comment, Comments] with AuthUserHandler {
  override implicit val format = Json.format[Comment]
}