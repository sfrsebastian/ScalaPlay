package controllers.traits

import auth.controllers.AuthUserHandler
import book.model.BookMin
import comment.model.CommentMin
import editorial.model._
import layers.controllers.CrudController
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/6/17.
  */
trait EditorialControllerTrait extends CrudController[EditorialDetail, Editorial, EditorialPersistenceModel, EditorialTable] with AuthUserHandler {

  implicit val formatCommentMin = Json.format[CommentMin]

  implicit val formatBookMin = Json.format[BookMin]

  implicit val formatDetail = Json.format[EditorialDetail]

  implicit def Detail2Model = EditorialDetailConverter
}