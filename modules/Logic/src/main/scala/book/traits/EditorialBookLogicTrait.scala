package book.traits

import book.model.{Book, BookPersistenceModel, BookTable}
import editorial.model.Editorial
import layers.logic.{CrudLogic, OneToManyLogic}

/**
  * Created by sfrsebastian on 5/30/17.
  */
trait EditorialBookLogicTrait extends CrudLogic[Book, BookPersistenceModel, BookTable] with OneToManyLogic[Editorial, Book, BookPersistenceModel, BookTable] {
  def inverseOneToManyRelationMapper(book:Book):Option[Editorial] = book.editorial
}
