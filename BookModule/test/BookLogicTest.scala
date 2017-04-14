import common.traits.test.CrudLogicTestTrait
import logic.bookModule.BookLogic
import models.bookModule.{Book, Books}
import org.mockito.Mockito._
import persistence.bookModule
import persistence.bookModule.BookPersistence
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/12/17.
  */
class BookLogicTest extends CrudLogicTestTrait[Book, Books, BookLogic, BookPersistence]{

  var persistenceMock = mock[BookPersistence]
  var logic = new BookLogic(persistenceMock)

  override def beforeEach(){
    persistenceMock = mock[BookPersistence]
    when(persistenceMock.table) thenReturn mock[TableQuery[Books]]
    logic = new BookLogic(persistenceMock)
  }

  override def generatePojo: Book = factory.manufacturePojo(classOf[Book])

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
        when(persistenceMock.get(null)) thenReturn Future(Some(newObject))
        whenReady(logic.create(newObject)) {
          case Some(_) => fail("No se deberia retornar el libro")
          case None => succeed
        }
      }

      "Se deberia retornar el libro creado si el ISBN dado no existe, los campos del libro retornado deben ser iguales que los del objeto creado" in {
        val newObject = generatePojo
        when(persistenceMock.get(null)) thenReturn Future(None)
        when(persistenceMock.create(newObject)) thenReturn Future(Some(newObject))
        whenReady(logic.create(newObject)) {
          case Some(element) => assertByProperties(element, newObject)
          case None => fail("Se deberia retorna el libro creado")
        }
      }
    }
  }
}
