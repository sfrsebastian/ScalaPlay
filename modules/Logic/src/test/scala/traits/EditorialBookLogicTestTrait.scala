package traits

import book.logic.EditorialBookLogic
import book.model.{Book, BookPersistenceModel, BookTable}
import book.persistence.BookPersistence
import editorial.model.Editorial
import org.mockito.Mockito.when
import slick.lifted.TableQuery
import tests.logic.OneToManyLogicTestTrait

/**
  * Created by sfrsebastian on 5/30/17.
  */
trait EditorialBookLogicTestTrait extends OneToManyLogicTestTrait[Editorial, Book, BookPersistenceModel, BookTable, EditorialBookLogic, BookPersistence] {
  var persistenceMock = mock[BookPersistence]

  var logic = new EditorialBookLogic(persistenceMock)

  override def beforeEach(){
    persistenceMock = mock[BookPersistence]
    when(persistenceMock.table) thenReturn mock[TableQuery[BookTable]]
    logic = new EditorialBookLogic(persistenceMock)
  }

  def generatePojos(sourceId: Int, destinationId: Int): (Editorial, Book) = {
    val editorial = factory.manufacturePojo(classOf[Editorial]).copy(id = sourceId, books = Seq())
    val book = factory.manufacturePojo(classOf[Book]).copy(id = destinationId, authors = Seq(), comments=Seq(), editorial = Some(editorial))
    (editorial.copy(books = Seq(book)), book)
  }
}
