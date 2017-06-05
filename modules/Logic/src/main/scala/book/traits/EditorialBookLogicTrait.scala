/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */

package book.traits

import book.model.{Book, BookPersistenceModel, BookTable}
import editorial.model.Editorial
import layers.logic.{CrudLogic, OneToManyLogic}

trait EditorialBookLogicTrait extends CrudLogic[Book, BookPersistenceModel, BookTable] with OneToManyLogic[Editorial, Book, BookPersistenceModel, BookTable] {
  def inverseOneToManyRelationMapper(book:Book):Option[Editorial] = book.editorial
}
