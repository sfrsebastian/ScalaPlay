/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package traits

import book.model._
import book.persistence.BookPersistence
import review.model.Review
import review.persistence.ReviewPersistence
import persistence.DatabasePopulator
import slick.jdbc.PostgresProfile.api._
import tests.persistence.CrudPersistenceTestTrait
import scala.util.Random

trait BookPersistenceTestTrait extends CrudPersistenceTestTrait[Book, BookPersistenceModel, BookTable]{

  override val tables = DatabasePopulator.tables

  override val persistence = new BookPersistence(new ReviewPersistence)

  override var seedCollection: Seq[Book] = Seq()

  override def generatePojo: Book = DatabasePopulator.generateBook(1).copy(authors = Random.shuffle(DatabasePopulator.authors).take(Random.nextInt(3) + 1))

  override implicit def Model2Persistence = BookPersistenceConverter

  override def populateDatabase = {
    val populate = DatabasePopulator.populate
    seedCollection = DatabasePopulator.books
    populate
  }

  override def assertByProperties(e1: BookPersistenceModel, e2: BookPersistenceModel, compareRefs:Boolean = true): Unit = {
    super.assertByProperties(e1, e2)
    assert(e1.description == e2.description, "La descripcion deberia ser la misma")
    assert(e1.ISBN == e2.ISBN, "El ISBN deberia ser el mismo")
    assert(e1.image == e2.image, "La imagen deberia ser la misma")
    assert(e1.editorialId == e2.editorialId, "La referencia a la editorial deberia ser la misma")
  }

  override def createTest: Unit = {
    super.createTest
    "Al insertar un nuevo libro" must {
      "Se deberian crear sus comentarios asociados" in {

        val newObject = generatePojo

        val Reviews:Seq[Review] = for {
          _ <- 0 to Random.nextInt(15) + 1
        }yield DatabasePopulator.generateReview.copy(book = newObject)

        whenReady(persistence.runAction(persistence.createAction(newObject.copy(Reviews = Reviews)))) { element =>
          whenReady(persistence.db.run(persistence.ReviewPersistence.table.filter(_.bookId === element.id).result)){ dbReviews =>
            assert(dbReviews.length == Reviews.length, "La longitud de los comentarios encontrados deberia ser la misma que los comentarios creados")
          }
        }
      }
    }
  }
}