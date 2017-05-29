import book.model._
import book.persistence.BookPersistence
import comment.model.Comment
import comment.persistence.CommentPersistence
import crud.tests.CrudPersistenceTestTrait
import persistence.DatabasePopulator
import slick.jdbc.PostgresProfile.api._
import scala.util.Random

trait BookPersistenceTestTrait extends CrudPersistenceTestTrait[Book, BookPersistenceModel, BookTable]{

  override val tables = DatabasePopulator.tables

  override val persistence = new BookPersistence(new CommentPersistence)

  override var seedCollection: Seq[Book] = Seq()

  override def generatePojo: Book = DatabasePopulator.generateBook(1).copy(authors = Random.shuffle(DatabasePopulator.authors).take(Random.nextInt(3) + 1))

  override implicit def Persistence2Model = BookPersistenceConverter

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

        val comments:Seq[Comment] = for {
          _ <- 0 to Random.nextInt(15) + 1
        }yield DatabasePopulator.generateComment.copy(book = newObject)

        whenReady(persistence.runAction(persistence.createAction(newObject.copy(comments = comments)))) { element =>
          whenReady(persistence.db.run(persistence.commentPersistence.table.filter(_.bookId === element.id).result)){ dbComments =>
            assert(dbComments.length == comments.length, "La longitud de los comentarios encontrados deberia ser la misma que los comentarios creados")
          }
        }
      }
    }
  }
}