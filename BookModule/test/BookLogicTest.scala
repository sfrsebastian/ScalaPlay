import logic.bookModule.BookLogic
import models.bookModule.{Book, Books}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import org.scalatestplus.play.PlaySpec
import persistence.bookModule.BookPersistenceTesting
import uk.co.jemos.podam.api.PodamFactoryImpl
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

/**
  * Created by sfrsebastian on 4/12/17.
  */
class BookLogicTest extends PlaySpec with BeforeAndAfterEach with ScalaFutures with MockitoSugar {
  val factory = new PodamFactoryImpl
  var table = mock[TableQuery[Books]]
  var persistenceMock = mock[BookPersistenceTesting]
  var logic = new BookLogic(persistenceMock)

  override def beforeEach(){
    persistenceMock = mock[BookPersistenceTesting]
    when(persistenceMock.table) thenReturn mock[TableQuery[Books]];
    logic = new BookLogic(persistenceMock)
  }

  "Al insertar un nuevo libro" must {
    "No se deberia retornar un libro si el ISBN dado ya existe" in {
      val nuevoLibro = factory.manufacturePojo(classOf[Book])
      when(persistenceMock.get(null)) thenReturn Future(Some(nuevoLibro))
      whenReady(logic.create(nuevoLibro)) { result => {
        result match {
          case Some(_) => fail("No se deberia retornar un elemento creado")
          case None => succeed
        }
      }}
    }

    "Se deberia retornar el libro creado si el ISBN dado no existe, los campos del objeto creado deben ser iguales que los del objeto creado" in {
      val nuevoLibro = factory.manufacturePojo(classOf[Book])
      when(persistenceMock.get(null)) thenReturn Future(None)
      when(persistenceMock.create(nuevoLibro)) thenReturn Future(Some(nuevoLibro))
      whenReady(logic.create(nuevoLibro)) { result => {
        result match {
          case Some(libro) => {
            assert(libro.name == nuevoLibro.name, "El nombre deberia ser el mismo");
            assert(libro.description == nuevoLibro.description, "La descripcion deberia ser la misma");
            assert(libro.ISBN == nuevoLibro.ISBN, "El ISBN deberia ser el mismo");
            assert(libro.image == nuevoLibro.image, "La imagen deberia ser la misma");
          }
          case None => fail("Se deberia retorna el libro creado")
        }
      }}
    }
  }

  "Al solicitar un libro" must {
    "Se deberia retorna el mismo objeto dado por la base de datos" in {
      val id = Random.nextInt(20)
      val mockBook = factory.manufacturePojo(classOf[Book])
      when(persistenceMock.get(anyInt())) thenReturn Future(Some(mockBook))
      whenReady(logic.get(id)){result => {
        result match {
          case Some(libro) => assert(libro == mockBook)
          case None => fail("El objeto buscado deberia encontrarse")
        }
      }}
    }
  }

  "Al solicitar multiples libros" must {
    "La lista de objetos recibida debe ser igual a la dada por la base de datos" in {
      val mockBooks = (0 to 20).map(_ => factory.manufacturePojo(classOf[Book]))
      when(persistenceMock.getAll) thenReturn Future(mockBooks)
      whenReady(logic.getAll) { result => {
        result match {
          case libros => {
            assert(libros == mockBooks, "Los libros recibidos deben ser los dados por la base de datos")
          }
          case Nil => fail("La lista no deberia ser vacia")
        }
      }}
    }
  }

  "Al actualizar un libro" must {
    "Se debe recibir el elemento actualizado en la base de datos en caso de que este exista" in {
      val actualizar = factory.manufacturePojo(classOf[Book])
      val id = Random.nextInt(20)
      when(persistenceMock.update(anyInt(), any(classOf[Book]))) thenReturn Future(Some(actualizar))
      whenReady(logic.update(id, actualizar)) { result => {
        result match{
          case Some(libro) => libro == actualizar
          case None => fail("Se debe recibir el libro actualizado")
        }
      }}
    }

    "Se debe recibir un elemento vacio debido a que no existe en la base de datos" in {
      val actualizar = factory.manufacturePojo(classOf[Book])
      val id = Random.nextInt(20)
      when(persistenceMock.update(anyInt(), any(classOf[Book]))) thenReturn Future(None)
      whenReady(logic.update(id, actualizar)) { result => {
        result match{
          case Some(_) => fail("No se deberia recibir un libro")
          case None => succeed
        }
      }}
    }
  }

  "Al eliminar un libro" must {
    "Se debe recibir el elemento eliminado en la base de datos en caso de que este exista" in {
      val eliminar = factory.manufacturePojo(classOf[Book])
      val id = Random.nextInt(20)
      when(persistenceMock.delete(anyInt())) thenReturn Future(Some(eliminar))
      whenReady(logic.delete(id)) { result => {
        result match{
          case Some(libro) => libro == eliminar
          case None => fail("Se debe recibir el libro eliminado")
        }
      }}
    }

    "Se debe recibir un elemento vacio debido a que no existe en la base de datos" in {
      val id = Random.nextInt(20)
      when(persistenceMock.delete(anyInt())) thenReturn Future(None)
      whenReady(logic.delete(id)) { result => {
        result match{
          case Some(_) => fail("No se deberia eliminar un elemento")
          case None => succeed
        }
      }}
    }
  }
}
