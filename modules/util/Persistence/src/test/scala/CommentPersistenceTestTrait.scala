import author.model.AuthorTable
import authorbook.model.AuthorBookTable
import book.model.{Book, BookPersistenceModel, BookTable}
import book.persistence.BookPersistence
import comment.model._
import comment.persistence.CommentPersistence
import crud.tests.CrudPersistenceTestTrait
import editorial.model.EditorialTable
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

trait CommentPersistenceTestTrait extends CrudPersistenceTestTrait[Comment, CommentPersistenceModel, CommentTable]{

  override val tables = Seq(
    TableQuery[EditorialTable],
    TableQuery[BookTable],
    TableQuery[AuthorTable],
    TableQuery[CommentTable],
    TableQuery[AuthorBookTable]
  )

  override val persistence = new CommentPersistence
  override var seedCollection: Seq[Comment] = Nil
  override def generatePojo : Comment = {
    val book = factory.manufacturePojo(classOf[Book]).copy(id = Random.nextInt(20) + 1)
    factory.manufacturePojo(classOf[Comment]).copy(book = book)
  }
  override implicit def Persistence2Model = CommentPersistenceConverter

  val bookPersistence = new BookPersistence(persistence)

  var bookCollection:Seq[BookPersistenceModel] = Nil
  val generateBookPojo = factory.manufacturePojo(classOf[BookPersistenceModel])

  override def populateDatabase = {
    seedCollection = Seq()
    bookCollection = Seq()

    bookCollection = for {
      _ <- 0 to 19
    }yield generateBookPojo
    val action1 = TableQuery[BookTable] ++= bookCollection

    seedCollection = for {
      _ <- 0 to 19
    }yield generatePojo
    val action2 = TableQuery[CommentTable] ++= seedCollection.map(c=>c:CommentPersistenceModel)

    DBIO.seq(action1, action2)
  }
}