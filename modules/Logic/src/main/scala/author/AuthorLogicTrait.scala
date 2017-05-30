package author.logic

import author.model._
import book.model.Book
import layers.logic.{CrudLogic, ManyToManyLogic}

/**
  * Created by sfrsebastian on 4/26/17.
  */
trait AuthorLogicTrait extends CrudLogic[Author, AuthorPersistenceModel, AuthorTable] with ManyToManyLogic[Book, Author, AuthorPersistenceModel, AuthorTable]{
  def inverseManyToManyRelationMapper(author:Author):Seq[Book] = author.books
}
