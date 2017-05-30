package tests

import akka.stream.Materializer
import crud.models.{Entity, ModelConverter, Row}
import layers.controllers.{CrudController, ManyToManyController}
import layers.logic.{CrudLogic, ManyToManyLogic}
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

/**
  * Created by sfrsebastian on 5/29/17.
  */
trait ManyToManyControllerTestTrait[S2<:Row,T2<:Row,K2<:Entity[T2], D, S<:Row,T<:Row,K<:Entity[T], C<:ManyToManyController[S2,T2,K2,D,S,T,K], H<:CrudLogic[S2,T2,K2], L<:CrudLogic[S,T,K] with ManyToManyLogic[S2,S,T,K]] extends PlaySpec with GuiceOneAppPerSuite with ScalaFutures with MockitoSugar {

  val factory = new PodamFactoryImpl

  implicit lazy val materializer: Materializer = app.materializer

  var sourceLogicMock: H

  var destinationLogicMock : L

  var controller: C

  implicit val formatDetail:Format[D]

  def generatePojos(sourceId:Int, destinationId:Int): (S2, S)

  implicit def Model2Detail:ModelConverter[S, D]

  implicit def M2S (f : D)(implicit converter : ModelConverter[S,D]) : S = converter.convertInverse(f)

  implicit def S2M (s: S)(implicit converter : ModelConverter[S,D]):D = converter.convert(s)

  def getResourcesFromSourceTest = {
    "Al solicitar los libros de un autor" must {
      "Se deberia retornar un listado de sus libros" in {
        val sourceResource = generatePojos(1,1)._1
        val destinationResource = (0 to 20).map(i => generatePojos(1, i)._2)
        val jsonResource = Json.toJson(destinationResource.map(e=>e:D))
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(Some(sourceResource))
        when(destinationLogicMock.getResourcesFromSource(sourceResource)) thenReturn Future(destinationResource)
        val result = call(controller.getResourcesFromSource(1), request)
        val jsonResponse = contentAsJson(result)
        assert(jsonResponse == jsonResource, "El json recibido debe corresponder al recurso obtenido")
        assert(status(result) == OK, "El codigo de respuesta debe ser 200")
      }

      "Se deberia retornar un mensaje de error si el origen de la relacion no existe" in {
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(None)
        val result = call(controller.getResourcesFromSource(1), request)
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }
    }
  }

  def getResourceFromSourceTest = {
    "Al solicitar un libro de un autor" must {
      "Se deberia retornar el libro solicitado" in {
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

  def addResourceToSourceTest = {
    "Al asociar un recurso destino al recurso origen" must {
      "Se deberia retornar el recurso destino asociado" in {
        val pojos = generatePojos(1, 1)
        val sourceResource = pojos._1
        val destinationResource = pojos._2
        val jsonResource = Json.toJson(destinationResource: D)
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(Some(sourceResource))
        when(destinationLogicMock.get(anyInt())) thenReturn Future(Some(destinationResource))
        when(destinationLogicMock.addResourceToSource(sourceResource, destinationResource)) thenReturn Future(Some(destinationResource))
        val result = call(controller.addResourceToSource(1, 1), request)
        val jsonResponse = contentAsJson(result)
        assert(jsonResponse == jsonResource, "El json recibido debe corresponder al recurso destino asociado")
        assert(status(result) == OK, "El codigo de respuesta debe ser 200")
      }

      "Se deberia retornar un mensaje de error si el origen de la relacion no existe" in {
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(None)
        val result = call(controller.addResourceToSource(1, 1), request)
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }

      "Se deberia retornar un mensaje de error si el destino de la relacion no existe" in {
        val pojos = generatePojos(1, 1)
        val sourceResource = pojos._1
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(Some(sourceResource))
        when(destinationLogicMock.get(anyInt())) thenReturn Future(None)
        val result = call(controller.addResourceToSource(1, 1), request)
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }

      "Se deberia retornar un mensaje de error si la asociación no es realizada exitosamente" in {
        val pojos = generatePojos(1, 1)
        val sourceResource = pojos._1
        val destinationResource = pojos._2
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(Some(sourceResource))
        when(destinationLogicMock.get(anyInt())) thenReturn Future(Some(destinationResource))
        when(destinationLogicMock.addResourceToSource(sourceResource, destinationResource)) thenReturn Future(None)
        val result = call(controller.addResourceToSource(1, 1), request)
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }
    }
  }

  def replaceResourcesFromSourceTest = {
    "Al reemplazar los recursos destino de un recurso origen" must {
      "Se deberia retornar la lista de recursos asociados" in {
        val sourceResource = generatePojos(1,1)._1
        val destinationResource = (0 to 20).map(i => generatePojos(1, i)._2)
        val jsonResource = Json.toJson(destinationResource.map(e=>e:D))
        val request = FakeRequest().withJsonBody(jsonResource)
        when(sourceLogicMock.get(anyInt())) thenReturn Future(Some(sourceResource))
        when(destinationLogicMock.replaceResourcesFromSource(any(), any())) thenReturn Future(destinationResource)
        val result = call(controller.replaceResourcesFromSource(1), request)
        val jsonResponse = contentAsJson(result)
        assert(jsonResponse == jsonResource, "El json recibido debe corresponder al recurso obtenido")
        assert(status(result) == OK, "El codigo de respuesta debe ser 200")
      }

      "Se deberia retornar un mensaje de error si el origen de la relacion no existe" in {
        val destinationResource = (0 to 20).map(i => generatePojos(1, i)._2)
        val jsonResource = Json.toJson(destinationResource.map(e=>e:D))
        val request = FakeRequest().withJsonBody(jsonResource)
        when(sourceLogicMock.get(anyInt())) thenReturn Future(None)
        val result = call(controller.replaceResourcesFromSource(1), request)
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }
    }
  }

  def deleteResourceFromSourceTest = {
    "Al eliminar un recurso destino del recurso origen" must {
      "Se deberia retornar el recurso eliminado" in {
        val pojos = generatePojos(1, 1)
        val sourceResource = pojos._1
        val destinationResource = pojos._2
        val jsonResource = Json.toJson(destinationResource: D)
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(Some(sourceResource))
        when(destinationLogicMock.get(anyInt())) thenReturn Future(Some(destinationResource))
        when(destinationLogicMock.removeResourceFromSource(sourceResource, destinationResource)) thenReturn Future(Some(destinationResource))
        val result = call(controller.deleteResourceFromSource(1, 1), request)
        val jsonResponse = contentAsJson(result)
        assert(jsonResponse == jsonResource, "El json recibido debe corresponder al recurso eliminado")
        assert(status(result) == OK, "El codigo de respuesta debe ser 200")
      }

      "Se deberia retornar un mensaje de error si el origen de la relacion no existe" in {
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(None)
        val result = call(controller.deleteResourceFromSource(1, 1), request)
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }

      "Se deberia retornar un mensaje de error si el destino de la relacion no existe" in {
        val pojos = generatePojos(1, 1)
        val sourceResource = pojos._1
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(Some(sourceResource))
        when(destinationLogicMock.get(anyInt())) thenReturn Future(None)
        val result = call(controller.deleteResourceFromSource(1, 1), request)
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }

      "Se deberia retornar un mensaje de error si el destino no hace parte de la colección del origen" in {
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(Some(generatePojos(1, 2)._1))
        when(destinationLogicMock.get(anyInt())) thenReturn Future(Some(generatePojos(2, 2)._2))
        val result = call(controller.deleteResourceFromSource(1, 1), request)
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }

      "Se deberia retornar el un mensaje de error si el recurso no pudo ser eliminado correctamente" in {
        val pojos = generatePojos(1, 1)
        val sourceResource = pojos._1
        val destinationResource = pojos._2
        val request = FakeRequest()
        when(sourceLogicMock.get(anyInt())) thenReturn Future(Some(sourceResource))
        when(destinationLogicMock.get(anyInt())) thenReturn Future(Some(destinationResource))
        when(destinationLogicMock.removeResourceFromSource(sourceResource, destinationResource)) thenReturn Future(None)
        val result = call(controller.deleteResourceFromSource(1, 1), request)
        assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
      }
    }
  }

  getResourceFromSourceTest
  getResourcesFromSourceTest
  addResourceToSourceTest
  replaceResourcesFromSourceTest
  deleteResourceFromSourceTest
}
