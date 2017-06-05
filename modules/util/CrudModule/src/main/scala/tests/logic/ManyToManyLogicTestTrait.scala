/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package tests.logic

import crud.models.{Entity, Row}
import layers.logic.ManyToManyLogic
import layers.persistence.{CrudPersistence, ManyToManyPersistence}
import org.mockito.ArgumentMatchers.{any, anyInt}
import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import slick.dbio.DBIO
import uk.co.jemos.podam.api.PodamFactoryImpl
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Trait genérico para probar la lógica de una relación muchos a muchos
  * @tparam S2 El modelo de negocio de la entidad origen
  * @tparam S El modelo de negocio de la entidad destino
  * @tparam T El modelo de persistencia de la entidad destino
  * @tparam K El modelo de tabla de la entidad destino
  * @tparam L La clase de la logica
  * @tparam P La persistencia de la logica
  */
trait ManyToManyLogicTestTrait [S2<:Row, S<:Row, T<:Row, K<:Entity[T], L<:ManyToManyLogic[S2,S,T,K], P<:CrudPersistence[S, T,K] with ManyToManyPersistence[S2,S]] extends PlaySpec with ScalaFutures with MockitoSugar {

  /**
    * Fábrica de creación de entidades Podam
    */
  val factory = new PodamFactoryImpl

  /**
    * Mock de persistencia de la entidad
    */
  var persistenceMock:P

  /**
    * La lógica a probar
    */
  var logic:L

  /**
    * Función que genera una dupla de la relación
    * @param sourceId El id de la entidad origen
    * @param destinationId El id de la entidad destino
    */
  def generatePojos(sourceId:Int, destinationId:Int): (S2, S)

  /**
    * Función que define la igualdad de dos entidades
    * @param e1 La entidad 1
    * @param e2 La entidad 2
    */
  def assertByProperties(e1:S, e2:S):Unit = assert(e1.name == e2.name, "El nombre deberia ser el mismo")

  def getResourcesFromSourceTest = {
    "Al solicitar los recursos del destino de la relacion" must {
      "Se deberia retornar el el listado de elementos dado por la base de datos" in {
        val pojos = generatePojos(1,1)
        val mockCollection = (0 to 20).map(i => generatePojos(1,i)._2)
        when(persistenceMock.getAllAction(any(), anyInt(),anyInt())) thenReturn mock[DBIO[Seq[S]]]
        when(persistenceMock.runAction(any[DBIO[Seq[S]]])) thenReturn Future(mockCollection.map(e=>e:S))
        whenReady(logic.getResourcesFromSource(pojos._1)) {
          case Nil => fail("Se deberia retornar el objeto creado")
          case elements => assert(elements == mockCollection.map(e=>e:S), "Los objetos recibidos deben ser los dados por la base de datos")
        }
      }
    }
  }

  def associateResourceToSource = {
    "Al asociar un elemento en la relacion" must {
      "Se deberia retornar el elemento asociado" in {
        val pojos = generatePojos(1,1)
        when(persistenceMock.associateEntityToSourceAction(any(), any())) thenReturn mock[DBIO[Option[S]]]
        when(persistenceMock.runAction(any[DBIO[Some[S]]])) thenReturn Future(Some(pojos._2))
        whenReady(logic.associateResourceToSource(pojos._1, pojos._2)) {
          case None => fail("Se deberia retornar el objeto creado")
          case Some(element) => assert(element == pojos._2, "Los objetos recibidos deben ser los dados por la base de datos")
        }
      }
    }
  }

  def disassociateResourceFromSource = {
    "Al desasociar un elemento de la relacion" must {
      "Se debe recibir el elemento eliminado en la base de datos" in {
        val pojos = generatePojos(1,1)
        when(persistenceMock.deleteAction(anyInt())) thenReturn mock[DBIO[Option[S]]]
        when(persistenceMock.runAction(any[DBIO[Some[S]]])) thenReturn Future(Some(pojos._2:S))
        whenReady(logic.disassociateResourceFromSource(pojos._1, pojos._2)) {
          case None => fail("Se debe recibir el objeto eliminado")
          case Some(element) => element == pojos._2
        }
      }
    }
  }

  getResourcesFromSourceTest
  associateResourceToSource
  disassociateResourceFromSource
}