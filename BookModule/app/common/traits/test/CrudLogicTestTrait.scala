package common.traits.test

import common.traits.app.{CrudLogic, CrudPersistence}
import common.traits.model.{Entity, Row}
import org.mockito.ArgumentMatchers.{any, anyInt}
import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import uk.co.jemos.podam.api.PodamFactoryImpl
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

/**
  * Created by sfrsebastian on 4/13/17.
  */
trait CrudLogicTestTrait[T<:Row, K<:Entity[T], L<:CrudLogic[T,K], P<:CrudPersistence[T,K]] extends PlaySpec with BeforeAndAfterEach with ScalaFutures with MockitoSugar with CrudTest {

  val factory = new PodamFactoryImpl
  var persistenceMock:P
  var logic:L

  def generatePojo:T

  def assertByProperties(e1:T, e2:T):Unit = assert(e1.name == e2.name, "El nombre deberia ser el mismo")

  def createTest: Unit ={
    "Al insertar un nuevo objeto" must {
      "Se deberia retornar el objeto creado en la base de datos" in {
        val newObject = generatePojo
        when(persistenceMock.get(null)) thenReturn Future(None)
        when(persistenceMock.create(any())) thenReturn Future(Some(newObject))
        whenReady(logic.create(newObject)) {
          case Some(element) => assertByProperties(element, newObject)
          case None => fail("Se deberia retornar el objeto creado")
        }
      }
    }
  }

  def getTest:Unit={
    "Al solicitar un objeto" must {
      "Se deberia retorna el objeto dado por la base de datos" in {
        val id = Random.nextInt(20)
        val dbObject = generatePojo
        when(persistenceMock.get(anyInt())) thenReturn Future(Some(dbObject))
        whenReady(logic.get(id)){
          case Some(element) => assert(element == dbObject)
          case None => fail("El objeto buscado deberia encontrarse")
        }
      }
    }
  }

  def getAllTest:Unit={
    "Al solicitar multiples objetos" must {
      "La lista de objetos recibida debe ser igual a la dada por la base de datos" in {
        val mockCollection = (0 to 20).map(_ => generatePojo)
        when(persistenceMock.getAll) thenReturn Future(mockCollection)
        whenReady(logic.getAll) {
          case elements => assert(elements == mockCollection, "Los objetos recibidos deben ser los dados por la base de datos")
          case Nil => fail("La coleccion no deberia ser vacia")
        }
      }
    }
  }

  def updateTest:Unit={
    "Al actualizar un objeto" must {
      "Se debe recibir el objeto actualizado en la base de datos" in {
        val toUpdate = generatePojo
        val id = Random.nextInt(20)
        when(persistenceMock.update(anyInt(), any())) thenReturn Future(Some(toUpdate))
        whenReady(logic.update(id, toUpdate)) {
          case Some(element) => element == toUpdate
          case None => fail("Se debe recibir el objeto actualizado")
        }
      }
    }
  }

  def deleteTest:Unit={
    "Al eliminar un objeto" must {
      "Se debe recibir el objeto eliminado en la base de datos" in {
        val toDelete = generatePojo
        val id = Random.nextInt(20)
        when(persistenceMock.delete(anyInt())) thenReturn Future(Some(toDelete))
        whenReady(logic.delete(id)) {
          case Some(element) => element == toDelete
          case None => fail("Se debe recibir el objeto eliminado")
        }
      }
    }
  }
}
