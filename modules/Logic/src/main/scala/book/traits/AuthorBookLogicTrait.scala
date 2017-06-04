package book.traits

import author.model.Author
import book.model.{Book, BookPersistenceModel, BookTable}
import layers.logic.{CrudLogic, ManyToManyLogic}

/**
  * Created by sfrsebastian on 5/30/17.
  */
trait AuthorBookLogicTrait extends CrudLogic[Book, BookPersistenceModel, BookTable] with ManyToManyLogic[Author, Book, BookPersistenceModel, BookTable] {
  def inverseRelationMapper(book:Book):Seq[Author] = book.authors
}
