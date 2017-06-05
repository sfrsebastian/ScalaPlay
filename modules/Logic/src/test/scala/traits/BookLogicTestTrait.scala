/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package traits

import book.logic.BookLogic
import book.model._
import book.persistence.BookPersistence
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery
import tests.logic.CrudLogicTestTrait
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait BookLogicTestTrait extends CrudLogicTestTrait[Book, BookPersistenceModel, BookTable, BookLogic, BookPersistence]{

  var persistenceMock = mock[BookPersistence]

  var logic = new BookLogic(persistenceMock)

  when(persistenceMock.table) thenReturn mock[TableQuery[BookTable]]

  override def generatePojo: Book = factory.manufacturePojo(classOf[Book]).copy(Reviews = Seq())

  override def assertByProperties(e1: Book, e2: Book): Unit = {
    super.assertByProperties(e1, e2)
    assert(e1.description == e2.description, "La descripcion deberia ser la misma")
    assert(e1.ISBN == e2.ISBN, "El ISBN deberia ser el mismo")
    assert(e1.image == e2.image, "La imagen deberia ser la misma")
  }

  override def createTest: Unit = {
    "Al insertar un nuevo libro" must {
      "No se deberia retornar un libro si el ISBN dado ya existe" in {
        val newObject = generatePojo
        when(persistenceMock.getAction(any())) thenReturn mock[DBIO[Option[Book]]]
        when(persistenceMock.runAction(any(classOf[DBIO[Option[Book]]]))) thenReturn Future(Some(newObject))
        whenReady(logic.create(newObject)) {
          case Some(_) => fail("No se deberia retornar el libro")
          case None => succeed
        }
      }

      "Se deberia retornar el libro creado si el ISBN dado no existe, los campos del libro retornado deben ser iguales que los del objeto creado" in {
        val newObject = generatePojo
        when(persistenceMock.getAction(any())) thenReturn mock[DBIO[Option[Book]]]
        when(persistenceMock.runAction(any[DBIO[Option[Nothing]]])) thenReturn Future(Option.empty)
        when(persistenceMock.runAction(persistenceMock.createAction(newObject))) thenReturn Future(newObject)
        whenReady(logic.create(newObject)) {
          case Some(element) => assertByProperties(element, newObject)
          case None => fail("Se deberia retorna el libro creado")
        }
      }
    }
  }
}
