/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */

package author.traits

import author.model.{Author, AuthorPersistenceModel, AuthorTable}
import book.model.Book
import layers.logic.{CrudLogic, ManyToManyLogic}

trait BookAuthorLogicTrait extends CrudLogic[Author, AuthorPersistenceModel, AuthorTable] with ManyToManyLogic[Book, Author,AuthorPersistenceModel, AuthorTable] {
  def inverseRelationMapper(author:Author):Seq[Book] = author.books
}
