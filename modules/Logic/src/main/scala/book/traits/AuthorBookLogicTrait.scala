/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package book.traits

import author.model.Author
import book.model.{Book, BookPersistenceModel, BookTable}
import layers.logic.{CrudLogic, ManyToManyLogic}

trait AuthorBookLogicTrait extends CrudLogic[Book, BookPersistenceModel, BookTable] with ManyToManyLogic[Author, Book, BookPersistenceModel, BookTable] {
  def inverseRelationMapper(book:Book):Seq[Author] = book.authors
}
