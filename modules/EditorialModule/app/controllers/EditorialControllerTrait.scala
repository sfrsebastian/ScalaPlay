package controllers.editorial

import auth.controllers.AuthUserHandler
import book.model.BookMin
import crud.layers.CrudController
import editorial.model.{Editorial, EditorialPersistenceModel, EditorialTable}
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/6/17.
  */
trait EditorialControllerTrait extends CrudController[Editorial, EditorialPersistenceModel, EditorialTable] with AuthUserHandler {
  implicit val BookMinformat = Json.format[BookMin]
  override implicit val format = Json.format[Editorial]
}