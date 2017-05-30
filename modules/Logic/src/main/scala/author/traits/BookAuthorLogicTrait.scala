package author.traits

import author.model.{Author, AuthorPersistenceModel, AuthorTable}
import book.model.Book
import layers.logic.{CrudLogic, ManyToManyLogic}

/**
  * Created by sfrsebastian on 5/30/17.
  */
trait BookAuthorLogicTrait extends CrudLogic[Author, AuthorPersistenceModel, AuthorTable] with ManyToManyLogic[Book, Author,AuthorPersistenceModel, AuthorTable] {
  def inverseManyToManyRelationMapper(author:Author):Seq[Book] = author.books
}
