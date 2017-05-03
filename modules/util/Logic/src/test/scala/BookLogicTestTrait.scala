import book.logic.BookLogic
import book.model._
import book.persistence.BookPersistence
import comment.logic.{CommentLogic, CommentLogicTrait}
import crud.models.ModelConverter
import crud.tests.CrudLogicTestTrait
import org.mockito.Mockito._
import slick.jdbc.PostgresProfile.api._

/**
  * Created by sfrsebastian on 4/12/17.
  */
trait BookLogicTestTrait extends CrudLogicTestTrait[Book, BookPersistenceModel, BookTable, BookLogic, BookPersistence]{

  var persistenceMock = mock[BookPersistence]
  var commentLogicMock:CommentLogicTrait = mock[CommentLogic]
  var logic = new BookLogic(persistenceMock, commentLogicMock)

  override implicit def Model2Persistence: ModelConverter[Book, BookPersistenceModel] = BookPersistenceConverter

  override implicit def Persistence2Model: ModelConverter[BookPersistenceModel, Book] = PersistenceBookConverter

  override def beforeEach(){
    persistenceMock = mock[BookPersistence]
    when(persistenceMock.table) thenReturn mock[TableQuery[BookTable]]
    logic = new BookLogic(persistenceMock, commentLogicMock)
  }

  override def generatePojo: BookPersistenceModel = factory.manufacturePojo(classOf[Book])

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
        //when(persistenceMock.get(null)) thenReturn Future(Some(newObject:BookPersistenceModel))
        whenReady(logic.create(newObject)) {
          case Some(_) => fail("No se deberia retornar el libro")
          case None => succeed
        }
      }

      "Se deberia retornar el libro creado si el ISBN dado no existe, los campos del libro retornado deben ser iguales que los del objeto creado" in {
        val newObject = generatePojo
        //when(persistenceMock.get(null)) thenReturn Future(None)
        //when(persistenceMock.create(newObject)) thenReturn Future(newObject:BookPersistenceModel)
        whenReady(logic.create(newObject)) {
          case Some(element) => assertByProperties(element, newObject)
          case None => fail("Se deberia retorna el libro creado")
        }
      }
    }
  }
}
