/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package traits

import author.model._
import author.persistence.AuthorPersistence
import book.model.Book
import book.persistence.BookPersistence
import review.persistence.ReviewPersistence
import persistence.DatabasePopulator
import slick.jdbc.PostgresProfile.api._
import tests.persistence.CrudPersistenceTestTrait
import scala.util.Random

trait AuthorPersistenceTestTrait extends CrudPersistenceTestTrait[Author, AuthorPersistenceModel, AuthorTable]{

  override val tables = DatabasePopulator.tables

  override val persistence =  new AuthorPersistence(new BookPersistence(new ReviewPersistence))

  override var seedCollection: Seq[Author] = Seq()

  override def generatePojo(): Author = DatabasePopulator.generateAuthor(1)

  override implicit val Model2Persistence = AuthorPersistenceConverter

  override def populateDatabase = {
    val populate = DatabasePopulator.populate
    seedCollection = DatabasePopulator.authors
    populate
  }

  override def assertByProperties(e1: AuthorPersistenceModel, e2: AuthorPersistenceModel, compareRefs:Boolean = true): Unit = {
    super.assertByProperties(e1, e2)
    assert(e1.lastName == e2.lastName, "El apellido deberia ser el mismo")
  }

  override def createTest: Unit = {
    super.createTest
    "Al insertar un nuevo autor" must {
      "Se deberian crear sus libros asociados" in {

        val newObject = generatePojo

        val books:Seq[Book] = for {
          _ <- 0 to Random.nextInt(5) + 1
        }yield DatabasePopulator.generateBook(1).copy(authors = newObject::Nil)

        whenReady(persistence.runAction(persistence.createAction(newObject.copy(books = books)))) { element =>
          whenReady(persistence.db.run(persistence.authorBookTable.filter(_.authorId === element.id).result)){ dbBooks =>
            assert(dbBooks.length == books.length, "La longitud de los libros encontrados deberia ser la misma que los libros creados")
          }
        }
      }
    }
  }
}
