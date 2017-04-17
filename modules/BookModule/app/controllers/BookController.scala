package controllers.book

import javax.inject.Inject
import crud.layers.CrudController
import book.logic.BookLogicTrait
import book.models.{Book, Books}
import play.api.libs.json.Json

class BookController @Inject()(override val logic:BookLogicTrait) extends CrudController[Book, Books] {
  override implicit val format = Json.format[Book]
}