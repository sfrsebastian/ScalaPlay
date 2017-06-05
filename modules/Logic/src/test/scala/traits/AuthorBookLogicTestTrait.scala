/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package traits

import author.model.Author
import book.logic.AuthorBookLogic
import book.model.{Book, BookPersistenceModel, BookTable}
import book.persistence.BookPersistence
import tests.logic.ManyToManyLogicTestTrait

trait AuthorBookLogicTestTrait extends ManyToManyLogicTestTrait[Author, Book, BookPersistenceModel, BookTable, AuthorBookLogic, BookPersistence] {
  var persistenceMock = mock[BookPersistence]

  var logic = new AuthorBookLogic(persistenceMock)

  def generatePojos(sourceId: Int, destinationId: Int): (Author, Book) = {
    val author = factory.manufacturePojo(classOf[Author]).copy(id = sourceId, books = Seq())
    val book = factory.manufacturePojo(classOf[Book]).copy(id = destinationId, authors = Seq(author), comments = Seq())
    (author.copy(books = Seq(book)), book)
  }
}
