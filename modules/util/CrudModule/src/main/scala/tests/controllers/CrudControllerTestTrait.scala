/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package tests.controllers

import akka.stream.Materializer
import crud.models.{Entity, ModelConverter, Row}
import crud.tests.CrudTest
import layers.controllers.CrudController
import layers.logic.CrudLogic
import org.mockito.ArgumentMatchers.{any, anyInt}
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
import scala.util.Random

/**
  * Trait genérico para probar el controlador de una entidad
  * @tparam D El modelo detale de la entidad
  * @tparam S El modelo de negocio de la entidad
  * @tparam T El modelo de persistencia de la entidad
  * @tparam K El modelo de tabla de la entidad
  * @tparam C La clase del controlador
  * @tparam L La logica del controlador
  */
trait CrudControllerTestTrait[D, S<:Row, T<:Row, K<:Entity[T], C<:CrudController[D,S,T,K], L<:CrudLogic[S,T,K]] extends PlaySpec with GuiceOneAppPerSuite with ScalaFutures with MockitoSugar with CrudTest{

  /**
    * Fábrica de creación de entidades Podam
    */
  val factory = new PodamFactoryImpl

  /**
    * Mock de logica del controlador
    */
  var logicMock:L

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
    * Función que genera una entidad
    */
  def generatePojo:S

  override def createTest = {
    "Al crear un nuevo recurso" must {
      "Se deberia retornar el mensaje esperado cuando el formato del recurso no corresponde al esperado" in {
        val request = FakeRequest().withJsonBody(Json.parse("{}"))
        val result = call(controller.create(), request)
        val response = contentAsString(result)
        assert(response == "Error en formato de contenido", "El mensaje deberia ser Error en formato de recurso")
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }

      "Se deberia retornar el mensaje esperado cuando el recurso no pudo ser creado" in {
        val newResource = generatePojo
        val request = FakeRequest().withJsonBody(Json.toJson(newResource:D))
        when(logicMock.create(any())) thenReturn Future(None)
        val result = call(controller.create(), request)
        val response = contentAsString(result)
        assert(response == "El recurso no pudo ser creado", "El mensaje deberia ser El recurso no pudo ser creado")
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }

      "Se deberia retornar el recurso creado cuando si pudo ser creado" in {
        val newResource = generatePojo
        val jsonResourceForm = Json.toJson(newResource:D)
        val jsonResourceMin = Json.toJson(newResource:D)
        val request = FakeRequest().withJsonBody(jsonResourceForm)
        when(logicMock.create(any())) thenReturn Future(Some(newResource))
        val result = call(controller.create(), request)
        val jsonResponse = contentAsJson(result)
        assert(jsonResponse == jsonResourceMin, "El json recibido debe corresponder al recurso obtenido por la logica")
        assert(status(result) == CREATED, "El codigo de respuesta debe ser 201")
      }
    }
  }

  override def getTest = {
    "Al solicitar un recurso" must {
      "Se debe recibir el json del recurso solicitado" in {
        val id = Random.nextInt(20)
        val newResource = generatePojo
        val request = FakeRequest()
        val jsonResource = Json.toJson(newResource:D)
        when(logicMock.get(anyInt())) thenReturn Future(Some(newResource))
        val result = call(controller.get(id), request)
        val jsonResponse = contentAsJson(result)
        assert(jsonResponse == jsonResource, "El json recibido debe corresponder al recurso obtenido por la logica")
        assert(status(result) == OK, "El codigo de respuesta debe ser 200")
      }

      "Se debe recibir el mensaje esperado cuando no existe el recurso" in {
        val id = Random.nextInt(20)
        val request = FakeRequest()
        when(logicMock.get(anyInt())) thenReturn Future(None)
        val result = call(controller.get(id), request)
        val response = contentAsString(result)
        assert(response == "El recurso no existe", "El mensaje deberia ser El recurso no existe")
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }
    }
  }

  override def getAllTest = {
    "Al solicitar todos los recursos" must {
      "Se debe recibir el json de la coleccion solicitada" in {
        val newResource = (0 to 20).map(_ => generatePojo)
        val jsonResource = Json.toJson(newResource.map(e=>e:D))
        val request = FakeRequest()
        when(logicMock.getAll(anyInt(), anyInt())) thenReturn Future(newResource)
        val result = call(controller.getAll(None, None), request)
        val jsonResponse = contentAsJson(result)
        assert(jsonResponse == jsonResource, "El json recibido debe corresponder al recurso obtenido")
        assert(status(result) == OK, "El codigo de respuesta debe ser 200")
      }
    }
  }

  override def updateTest = {
    "Al actualizar un recurso" must {
      "Se deberia retornar el mensaje esperado cuando el formato del recurso no corresponde al esperado" in {
        val request = FakeRequest().withJsonBody(Json.parse("{}"))
        val id = Random.nextInt(20)
        val result = call(controller.update(id), request)
        val response = contentAsString(result)
        assert(response == "Error en formato de contenido", "El mensaje deberia ser Error en formato de contenido")
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }

      "Se deberia retornar el mensaje esperado cuando el elemento no pudo ser actualizado" in {
        val toUpdate = generatePojo
        val id = Random.nextInt(20)
        val request = FakeRequest().withJsonBody(Json.toJson(toUpdate:D))
        when(logicMock.update(anyInt(), any())) thenReturn Future(None)
        val result = call(controller.update(id), request)
        val response = contentAsString(result)
        assert(response == "El recurso no pudo ser actualizado", "El mensaje deberia ser El recurso no pudo ser actualizado")
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }

      "Se deberia retornar el elemento creado cuando si pudo ser actualizado" in {
        val toUpdate = generatePojo
        val jsonResource = Json.toJson(toUpdate:D)
        val id = Random.nextInt(20)
        val request = FakeRequest().withJsonBody(Json.toJson(toUpdate:D))
        when(logicMock.update(anyInt(), any())) thenReturn Future(Some(toUpdate))
        val result = call(controller.update(id), request)
        val jsonResponse = contentAsJson(result)
        assert(jsonResponse == jsonResource, "El json recibido debe corresponder al recurso obtenido")
        assert(status(result) == OK, "El codigo de respuesta debe ser 200")
      }
    }
  }

  override def deleteTest = {
    "Al eliminar un recurso" must {
      "Se deberia retornar el mensaje esperado cuando el recurso no pudo ser eliminado" in {
        val id = Random.nextInt(20)
        val request = FakeRequest()
        when(logicMock.delete(anyInt())) thenReturn Future(None)
        val result = call(controller.delete(id), request)
        val response = contentAsString(result)
        assert(response == "El recurso con id dado no existe", "El mensaje deberia ser El recurso con id dado no existe")
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }

      "Se deberia retornar el recurso eliminado cuando la operacion es exitosa" in {
        val toDelete = generatePojo
        val jsonResource = Json.toJson(toDelete:D)
        val id = Random.nextInt(20)
        val request = FakeRequest()
        when(logicMock.delete(anyInt())) thenReturn Future(Some(toDelete))
        val result = call(controller.delete(id), request)
        val jsonResponse = contentAsJson(result)
        assert(jsonResponse == jsonResource, "El json recibido debe corresponder al recurso eliminado")
        assert(status(result) == OK, "El codigo de respuesta debe ser 200")
      }
    }
  }
}