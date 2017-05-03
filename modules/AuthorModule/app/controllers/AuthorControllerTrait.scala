package controllers.author

import auth.controllers.AuthUserHandler
import author.logic.AuthorLogicTrait
import crud.layers.CrudController
import author.model._
import play.api.libs.json.Json
import model.ModelImplicits._

/**
  * Created by sfrsebastian on 5/2/17.
  */
trait AuthorControllerTrait extends CrudController[Author, AuthorPersistenceModel, AuthorTable] with AuthUserHandler {

  val logic:AuthorLogicTrait
  override implicit val format = Json.format[Author]

}
