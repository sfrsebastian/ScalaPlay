package book.logic

import crud.layers.CrudLogic
import book.models.{Book, Books}
import book.persistence.BookPersistenceTrait

trait BookLogicTrait extends CrudLogic[Book, Books] {
  val persistence : BookPersistenceTrait
}
