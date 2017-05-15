package controllers.author

import auth.controllers.AuthUserHandler
import author.AuthorDetail
import author.logic.AuthorLogicTrait
import crud.layers.CrudController
import author.model._
import book.model.BookMin
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/2/17.
  */
trait AuthorControllerTrait extends CrudController[AuthorDetail, Author, AuthorPersistenceModel, AuthorTable] with AuthUserHandler {

  val logic:AuthorLogicTrait

  implicit val bookMinFormat=Json.format[BookMin]

  implicit val formatDetail = Json.format[AuthorDetail]

  implicit def Detail2Model = AuthorDetailConverter
}
