/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package traits

import book.persistence.BookPersistence
import review.model._
import review.persistence.ReviewPersistence
import persistence.DatabasePopulator
import tests.persistence.CrudPersistenceTestTrait
import scala.util.Random

trait ReviewPersistenceTestTrait extends CrudPersistenceTestTrait[Review, ReviewPersistenceModel, ReviewTable]{

  override val tables = DatabasePopulator.tables

  override val persistence = new ReviewPersistence
  val bookPersistence = new BookPersistence(persistence)

  override var seedCollection: Seq[Review] = Seq()

  override def generatePojo : Review = {
    DatabasePopulator.generateReview.copy(book = Random.shuffle(DatabasePopulator.books).take(1).head)
  }

  override implicit val Model2Persistence = ReviewPersistenceConverter

  override def populateDatabase = {
    val populate = DatabasePopulator.populate
    seedCollection = DatabasePopulator.Reviews
    populate
  }

  override def assertByProperties(e1: ReviewPersistenceModel, e2: ReviewPersistenceModel, compareRefs:Boolean = true): Unit = {
    super.assertByProperties(e1, e2)
    assert(e1.content == e2.content, "El contenido deberia ser el mismo")
    if(compareRefs){
      assert(e1.bookId == e2.bookId, "El libro referenciado deberia ser el mismo")
    }
  }
}