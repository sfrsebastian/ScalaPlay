package tests.logic

import crud.models.{Entity, Row}
import crud.tests.CrudTest
import layers.logic.CrudLogic
import layers.persistence.CrudPersistence
import org.mockito.ArgumentMatchers.{any, anyInt}
import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import slick.jdbc.PostgresProfile.api._
import uk.co.jemos.podam.api.PodamFactoryImpl

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

/**
  * Created by sfrsebastian on 4/13/17.
  */
trait CrudLogicTestTrait[S<:Row, T<:Row, K<:Entity[T], L<:CrudLogic[S, T,K], P<:CrudPersistence[S, T,K]] extends PlaySpec with BeforeAndAfterEach with ScalaFutures with MockitoSugar with CrudTest {

  val factory = new PodamFactoryImpl

  var persistenceMock:P

  var logic:L

  def generatePojo:S

  def assertByProperties(e1:S, e2:S):Unit = assert(e1.name == e2.name, "El nombre deberia ser el mismo")

  def createTest: Unit ={
    "Al insertar un nuevo objeto" must {
      "Se deberia retornar el objeto creado en la base de datos" in {
        val newObject = generatePojo
        when(persistenceMock.runAction(persistenceMock.createAction(newObject))) thenReturn Future(newObject)
        whenReady(logic.create(newObject)) {
          case None => fail("Se deberia retornar el objeto creado")
          case Some(element) => assertByProperties(element, newObject)
        }
      }
    }
  }

  def getTest:Unit={
    "Al solicitar un objeto" must {
      "Se deberia retorna el objeto dado por la base de datos" in {
        val id = Random.nextInt(20)
        val dbObject = generatePojo
        when(persistenceMock.getAction(any())) thenReturn mock[DBIO[Option[S]]]
        when(persistenceMock.runAction(any[DBIO[Some[S]]])) thenReturn Future(Some(dbObject:S))
        whenReady(logic.get(id)){
          case None => fail("El objeto buscado deberia encontrarse")
          case Some(element) => assert(element == (dbObject:S))
        }
      }
    }
  }

  def getAllTest:Unit={
    "Al solicitar multiples objetos" must {
      "La lista de objetos recibida debe ser igual a la dada por la base de datos" in {
        val mockCollection = (0 to 20).map(_ => generatePojo)
        when(persistenceMock.getAllAction(any(), anyInt(),anyInt())) thenReturn mock[DBIO[Seq[S]]]
        when(persistenceMock.runAction(any[DBIO[Seq[S]]])) thenReturn Future(mockCollection.map(e=>e:S))
        whenReady(logic.getAll(0, 100)) {
          case Nil => fail("La coleccion no deberia ser vacia")
          case elements => assert(elements == mockCollection.map(e=>e:S), "Los objetos recibidos deben ser los dados por la base de datos")
        }
      }
    }
  }

  def updateTest:Unit={
    "Al actualizar un objeto" must {
      "Se debe recibir el objeto actualizado en la base de datos" in {
        val toUpdate = generatePojo
        val id = Random.nextInt(20)
        when(persistenceMock.updateAction(anyInt(), any())) thenReturn mock[DBIO[Option[S]]]
        when(persistenceMock.runAction(any[DBIO[Some[S]]])) thenReturn Future(Some(toUpdate:S))
        whenReady(logic.update(id, toUpdate)) {
          case None => fail("Se debe recibir el objeto actualizado")
          case Some(element) => element == toUpdate
        }
      }
    }
  }

  def deleteTest:Unit={
    "Al eliminar un objeto" must {
      "Se debe recibir el objeto eliminado en la base de datos" in {
        val toDelete = generatePojo
        val id = Random.nextInt(20)
        when(persistenceMock.deleteAction(anyInt())) thenReturn mock[DBIO[Option[S]]]
        when(persistenceMock.runAction(any[DBIO[Some[S]]])) thenReturn Future(Some(toDelete:S))
        whenReady(logic.delete(id)) {
          case None => fail("Se debe recibir el objeto eliminado")
          case Some(element) => element == toDelete
        }
      }
    }
  }
}
