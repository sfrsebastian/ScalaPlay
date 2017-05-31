package traits

import author.model.Author
import book.logic.AuthorBookLogic
import book.model.{Book, BookPersistenceModel, BookTable}
import book.persistence.BookPersistence
import org.mockito.Mockito.when
import slick.lifted.TableQuery
import tests.logic.ManyToManyLogicTestTrait

/**
  * Created by sfrsebastian on 5/30/17.
  */
trait AuthorBookLogicTestTrait extends ManyToManyLogicTestTrait[Author, Book, BookPersistenceModel, BookTable, AuthorBookLogic, BookPersistence] {
  var persistenceMock = mock[BookPersistence]

  var logic = new AuthorBookLogic(persistenceMock)

  override def beforeEach(){
    persistenceMock = mock[BookPersistence]
    when(persistenceMock.table) thenReturn mock[TableQuery[BookTable]]
    logic = new AuthorBookLogic(persistenceMock)
  }

  def generatePojos(sourceId: Int, destinationId: Int): (Author, Book) = {
    val author = factory.manufacturePojo(classOf[Author]).copy(id = sourceId, books = Seq())
    val book = factory.manufacturePojo(classOf[Book]).copy(id = destinationId, authors = Seq(author), comments = Seq())
    (author.copy(books = Seq(book)), book)
  }
}
