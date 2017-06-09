/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package tests.controllers

import akka.stream.Materializer
import crud.models.{Entity, ModelConverter, Row}
import layers.controllers.ManyToManyController
import layers.logic.{CrudLogic, ManyToManyLogic}
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.{Format, Json}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.co.jemos.podam.api.PodamFactoryImpl
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Trait genérico para probar el controlador de una relación muchos a muchos
  * @tparam S2 El modelo de negocio de la entidad origen
  * @tparam T2 El modelo de persistencia de la entidad origen
  * @tparam K2 El modelo de tabla de la entidad origen
  * @tparam D El modelo detalle de la entidad destino
  * @tparam S El modelo de negocio de la entidad destino
  * @tparam T El modelo de persistencia de la entidad destino
  * @tparam K El modelo de tabla de la entidad destino
  * @tparam C La clase del controlador
  * @tparam H La logica de la entidad origen
  * @tparam L La logica de la entidad destino
  */
trait ManyToManyControllerTestTrait[S2<:Row,T2<:Row,K2<:Entity[T2], D, S<:Row,T<:Row,K<:Entity[T], C<:ManyToManyController[S2,T2,K2,D,S,T,K], H<:CrudLogic[S2,T2,K2], L<:CrudLogic[S,T,K] with ManyToManyLogic[S2,S,T,K]] extends PlaySpec with GuiceOneAppPerSuite with ScalaFutures with MockitoSugar {

  /**
    * Fábrica de creación de entidades Podam
    */
  val factory = new PodamFactoryImpl

  /**
    * Mock de logica de la entidad origen
    */
  var sourceLogicMock: H

  /**
    * Mock de logica de la entidad destino
    */
  var destinationLogicMock : L

  /**
    * El controlador a probar
    */
  var controller: C

  /**
    * El formato de tipo detalle de la entidad
    */
  implicit val formatDetail:Format[D]

  implicit lazy val materializer: Materializer = app.materializer

  /**
    * Convertidor de modelo de negocio a modelo detalle
    */
  implicit val Model2Detail:ModelConverter[S, D]

  /**
    * Convierte el modelo detalle a modelo de negocio
    * @param d El modelo detalle
    * @param converter El convertidor a utilizar
    * @return Representación en modelo de negocio del modelo detalle dado
    */
  implicit def D2S (d : D)(implicit converter : ModelConverter[S,D]) : S = converter.convertInverse(d)

  /**
    * Convierte el modelo de negocio a modelo de detalle
    * @param s El modelo negocio
    * @param converter El convertidor a utilizar
    * @return Representación en modelo de detalle del modelo negocio dado
    */
  implicit def S2D (s: S)(implicit converter : ModelConverter[S,D]):D = converter.convert(s)

  /**
    * Función que genera una dupla de la relación
    * @param sourceId El id de la entidad origen
    * @param destinationId El id de la entidad destino
    */
  def generatePojos(sourceId:Int, destinationId:Int): (S2, S)

  def getResourcesFromSourceTest = {
    "Al solicitar los recursos destino de una relacion" must {
      "Se deberia retornar un listado de sus recursos destino" in {
        val sourceResource = generatePojos(1,1)._1
        val destinationResource = (0 to 20).map(i => generatePojos(1, i)._2)
        val jsonResource = Json.toJson(destinationResource.map(e=>e:D))
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(Some(sourceResource))
        when(destinationLogicMock.getResourcesFromSource(sourceResource, 0, Int.MaxValue)) thenReturn Future(destinationResource)
        val result = call(controller.getResourcesFromSource(1, None, None), request)
        val jsonResponse = contentAsJson(result)
        assert(jsonResponse == jsonResource, "El json recibido debe corresponder al recurso obtenido")
        assert(status(result) == OK, "El codigo de respuesta debe ser 200")
      }

      "Se deberia retornar un mensaje de error si el origen de la relacion no existe" in {
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(None)
        val result = call(controller.getResourcesFromSource(1, None, None), request)
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }
    }
  }

  def getResourceFromSourceTest = {
    "Al solicitar un recurso destino de la relacion" must {
      "Se deberia retornar el recurso solicitado" in {
        val pojos = generatePojos(1, 1)
        val sourceResource = pojos._1
        val destinationResource = pojos._2
        val jsonResource = Json.toJson(destinationResource: D)
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(Some(sourceResource))
        when(destinationLogicMock.get(anyInt())) thenReturn Future(Some(destinationResource))
        val result = call(controller.getResourceFromSource(1, 1), request)
        val jsonResponse = contentAsJson(result)
        assert(jsonResponse == jsonResource, "El json recibido debe corresponder al recurso obtenido")
        assert(status(result) == OK, "El codigo de respuesta debe ser 200")
      }

      "Se deberia retornar un mensaje de error si el origen de la relacion no existe" in {
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(None)
        val result = call(controller.getResourceFromSource(1, 1), request)
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }

      "Se deberia retornar un mensaje de error si el destino de la relacion no existe" in {
        val pojos = generatePojos(1, 1)
        val sourceResource = pojos._1
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(Some(sourceResource))
        when(destinationLogicMock.get(anyInt())) thenReturn Future(None)
        val result = call(controller.getResourceFromSource(1, 1), request)
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }

      "Se deberia retornar un mensaje de error si el destino no hace parte de la colección del origen" in {
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(Some(generatePojos(1, 2)._1))
        when(destinationLogicMock.get(anyInt())) thenReturn Future(Some(generatePojos(2, 2)._2))
        val result = call(controller.getResourceFromSource(1, 1), request)
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }
    }
  }

  def associateResourceToSourceTest = {
    "Al asociar un recurso destino al recurso origen" must {
      "Se deberia retornar el recurso destino asociado" in {
        val pojos1 = generatePojos(1, 1)
        val pojos2 = generatePojos(1,2)
        val sourceResource = pojos1._1
        val destinationResource = pojos2._2
        val jsonResource = Json.toJson(destinationResource: D)
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(Some(sourceResource))
        when(destinationLogicMock.get(anyInt())) thenReturn Future(Some(destinationResource))
        when(destinationLogicMock.associateResourceToSource(sourceResource, destinationResource)) thenReturn Future(Some(destinationResource))
        val result = call(controller.associateResourceToSource(1, 2), request)
        val jsonResponse = contentAsJson(result)
        assert(jsonResponse == jsonResource, "El json recibido debe corresponder al recurso destino asociado")
        assert(status(result) == OK, "El codigo de respuesta debe ser 200")
      }

      "Se deberia retornar un mensaje de error si el origen de la relacion no existe" in {
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(None)
        val result = call(controller.associateResourceToSource(1, 1), request)
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }

      "Se deberia retornar un mensaje de error si el destino de la relacion no existe" in {
        val pojos = generatePojos(1, 1)
        val sourceResource = pojos._1
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(Some(sourceResource))
        when(destinationLogicMock.get(anyInt())) thenReturn Future(None)
        val result = call(controller.associateResourceToSource(1, 1), request)
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }

      "Se deberia retornar un mensaje de error si la asociación no es realizada exitosamente" in {
        val pojos = generatePojos(1, 1)
        val sourceResource = pojos._1
        val destinationResource = pojos._2
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(Some(sourceResource))
        when(destinationLogicMock.get(anyInt())) thenReturn Future(Some(destinationResource))
        when(destinationLogicMock.associateResourceToSource(sourceResource, destinationResource)) thenReturn Future(None)
        val result = call(controller.associateResourceToSource(1, 1), request)
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }
    }
  }

  def disassociateResourceFromSourceTest = {
    "Al eliminar un recurso destino del recurso origen" must {
      "Se deberia retornar el recurso eliminado" in {
        val pojos = generatePojos(1, 1)
        val sourceResource = pojos._1
        val destinationResource = pojos._2
        val jsonResource = Json.toJson(destinationResource: D)
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(Some(sourceResource))
        when(destinationLogicMock.get(anyInt())) thenReturn Future(Some(destinationResource))
        when(destinationLogicMock.disassociateResourceFromSource(sourceResource, destinationResource)) thenReturn Future(Some(destinationResource))
        val result = call(controller.disassociateResourceFromSource(1, 1), request)
        val jsonResponse = contentAsJson(result)
        assert(jsonResponse == jsonResource, "El json recibido debe corresponder al recurso eliminado")
        assert(status(result) == OK, "El codigo de respuesta debe ser 200")
      }

      "Se deberia retornar un mensaje de error si el origen de la relacion no existe" in {
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(None)
        val result = call(controller.disassociateResourceFromSource(1, 1), request)
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }

      "Se deberia retornar un mensaje de error si el destino de la relacion no existe" in {
        val pojos = generatePojos(1, 1)
        val sourceResource = pojos._1
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(Some(sourceResource))
        when(destinationLogicMock.get(anyInt())) thenReturn Future(None)
        val result = call(controller.disassociateResourceFromSource(1, 1), request)
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }

      "Se deberia retornar un mensaje de error si el destino no hace parte de la colección del origen" in {
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(Some(generatePojos(1, 2)._1))
        when(destinationLogicMock.get(anyInt())) thenReturn Future(Some(generatePojos(2, 2)._2))
        val result = call(controller.disassociateResourceFromSource(1, 1), request)
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }

      "Se deberia retornar el un mensaje de error si el recurso no pudo ser eliminado correctamente" in {
        val pojos = generatePojos(1, 1)
        val sourceResource = pojos._1
        val destinationResource = pojos._2
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(Some(sourceResource))
        when(destinationLogicMock.get(anyInt())) thenReturn Future(Some(destinationResource))
        when(destinationLogicMock.disassociateResourceFromSource(sourceResource, destinationResource)) thenReturn Future(None)
        val result = call(controller.disassociateResourceFromSource(1, 1), request)
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }
    }
  }

  getResourceFromSourceTest
  getResourcesFromSourceTest
  associateResourceToSourceTest
  disassociateResourceFromSourceTest
}