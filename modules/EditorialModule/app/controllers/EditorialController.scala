package controllers.editorial

import editorial.logic.EditorialLogicTrait
import editorial.models.{Editorial, Editorials}
import com.google.inject.Inject
import crud.layers.CrudController
import play.api.libs.json.Json
import auth.controllers.AuthUserHandler

/**
  * Created by sfrsebastian on 4/26/17.
  */
class EditorialController @Inject()(override val logic:EditorialLogicTrait) extends CrudController[Editorial, Editorials] with AuthUserHandler {
  override implicit val format = Json.format[Editorial]
}