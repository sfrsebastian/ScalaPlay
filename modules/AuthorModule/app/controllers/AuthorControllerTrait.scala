package controllers.author

import auth.controllers.AuthUserHandler
import author.AuthorForm
import author.logic.AuthorLogicTrait
import crud.layers.CrudController
import author.model._
import book.model.BookMin
import comment.model.CommentMin
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/2/17.
  */
trait AuthorControllerTrait extends CrudController[AuthorForm, AuthorMin, Author, AuthorPersistenceModel, AuthorTable] with AuthUserHandler {

  val logic:AuthorLogicTrait

  implicit val commentMinFormat = Json.format[CommentMin]

  implicit val bookMinFormat=Json.format[BookMin]

  implicit val formatMin = Json.format[AuthorMin]

  implicit val formatForm = Json.format[AuthorForm]

  implicit def Min2Model = AuthorMinConverter

  implicit def Form2Model = AuthorFormConverter
}
