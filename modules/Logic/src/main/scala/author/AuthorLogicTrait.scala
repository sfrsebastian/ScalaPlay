package author.logic
import author.persistence.AuthorPersistenceTrait
import author.model._
import book.model.{Book, BookPersistenceModel, BookTable}
import layers.logic.{CrudLogic, ManyToManyLogic}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/26/17.
  */
trait AuthorLogicTrait extends CrudLogic[Author, AuthorPersistenceModel, AuthorTable] with ManyToManyLogic[Book, Author, AuthorPersistenceModel, AuthorTable]{
  val persistence : AuthorPersistenceTrait
  def inverseRelationMapper(author:Author):Seq[Book] = author.books
}
