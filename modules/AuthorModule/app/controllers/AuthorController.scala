package controllers.author

import author.logic.AuthorLogicTrait
import models.author._
import com.google.inject.Inject
import crud.layers.CrudController
import play.api.libs.json.Json
import auth.controllers.AuthUserHandler
import models.ModelImplicits._

/**
  * Created by sfrsebastian on 4/26/17.
  */
class AuthorController @Inject()(override val logic:AuthorLogicTrait) extends CrudController[Author, AuthorPersistenceModel, AuthorTable] with AuthUserHandler {
  override implicit val format = Json.format[Author]
}