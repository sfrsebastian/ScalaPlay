import book.persistence.BookPersistence
import comment.model._
import comment.persistence.CommentPersistence
import persistence.DatabasePopulator
import tests.persistence.CrudPersistenceTestTrait

import scala.util.Random

trait CommentPersistenceTestTrait extends CrudPersistenceTestTrait[Comment, CommentPersistenceModel, CommentTable]{

  override val tables = DatabasePopulator.tables

  override val persistence = new CommentPersistence
  val bookPersistence = new BookPersistence(persistence)

  override var seedCollection: Seq[Comment] = Seq()

  override def generatePojo : Comment = {
    DatabasePopulator.generateComment.copy(book = Random.shuffle(DatabasePopulator.books).take(1).head)
  }

  override implicit val Model2Persistence = CommentPersistenceConverter

  override def populateDatabase = {
    val populate = DatabasePopulator.populate
    seedCollection = DatabasePopulator.comments
    populate
  }

  override def assertByProperties(e1: CommentPersistenceModel, e2: CommentPersistenceModel, compareRefs:Boolean = true): Unit = {
    super.assertByProperties(e1, e2)
    assert(e1.content == e2.content, "El contenido deberia ser el mismo")
    if(compareRefs){
      assert(e1.bookId == e2.bookId, "El libro referenciado deberia ser el mismo")
    }
  }
}