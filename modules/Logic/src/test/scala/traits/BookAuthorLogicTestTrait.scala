/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package traits

import author.BookAuthorLogic
import author.model.{Author, AuthorPersistenceModel, AuthorTable}
import author.persistence.AuthorPersistence
import book.model.Book
import org.mockito.Mockito.when
import slick.lifted.TableQuery
import tests.logic.ManyToManyLogicTestTrait

trait BookAuthorLogicTestTrait extends ManyToManyLogicTestTrait[Book, Author, AuthorPersistenceModel, AuthorTable, BookAuthorLogic, AuthorPersistence] {
  var persistenceMock = mock[AuthorPersistence]

  var logic = new BookAuthorLogic(persistenceMock)

  when(persistenceMock.table) thenReturn mock[TableQuery[AuthorTable]]

  def generatePojos(sourceId: Int, destinationId: Int): (Book, Author) = {
    val book = factory.manufacturePojo(classOf[Book]).copy(id = sourceId, authors = Seq(), reviews = Seq())
    val author = factory.manufacturePojo(classOf[Author]).copy(id = destinationId, books = Seq(book))
    (book.copy(authors = Seq(author)), author)
  }
}
