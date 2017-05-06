package controllers.editorial

import auth.controllers.AuthUserHandler
import author.model.AuthorMin
import book.model.BookMin
import comment.model.CommentMin
import crud.layers.CrudController
import editorial.model._
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/6/17.
  */
trait EditorialControllerTrait extends CrudController[EditorialForm, EditorialMin, Editorial, EditorialPersistenceModel, EditorialTable] with AuthUserHandler {

  implicit val formatCommentMin = Json.format[CommentMin]

  implicit val formatBookMin = Json.format[BookMin]

  implicit val formatMin = Json.format[EditorialMin]

  implicit val formatForm = Json.format[EditorialForm]

  implicit def Min2Model = EditorialMinConverter

  implicit def Form2Model = EditorialFormConverter
}