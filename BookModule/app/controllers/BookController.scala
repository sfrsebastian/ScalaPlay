package controllers.bookModule

import javax.inject.Inject

import common.traits.layers.CrudController
import logic.bookModule.BookLogicTrait
import models.bookModule.{Book, Books}
import play.api.libs.json.Json

class BookController @Inject()(override val logic:BookLogicTrait) extends CrudController[Book,Books] {
  override implicit val format = Json.format[Book]
}