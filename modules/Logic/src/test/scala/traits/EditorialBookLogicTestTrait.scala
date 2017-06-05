/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package traits

import book.logic.EditorialBookLogic
import book.model.{Book, BookPersistenceModel, BookTable}
import book.persistence.BookPersistence
import editorial.model.Editorial
import org.mockito.Mockito.when
import slick.lifted.TableQuery
import tests.logic.OneToManyLogicTestTrait

trait EditorialBookLogicTestTrait extends OneToManyLogicTestTrait[Editorial, Book, BookPersistenceModel, BookTable, EditorialBookLogic, BookPersistence] {
  var persistenceMock = mock[BookPersistence]

  var logic = new EditorialBookLogic(persistenceMock)

  when(persistenceMock.table) thenReturn mock[TableQuery[BookTable]]

  def generatePojos(sourceId: Int, destinationId: Int): (Editorial, Book) = {
    val editorial = factory.manufacturePojo(classOf[Editorial]).copy(id = sourceId, books = Seq())
    val book = factory.manufacturePojo(classOf[Book]).copy(id = destinationId, authors = Seq(), comments=Seq(), editorial = Some(editorial))
    (editorial.copy(books = Seq(book)), book)
  }
}
