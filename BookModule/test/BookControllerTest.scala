import akka.stream.Materializer
import common.traits.app.CrudLogic
import common.traits.test.CrudControllerTestTrait
import controllers.bookModule.BookController
import logic.bookModule.BookLogic
import models.bookModule.{Book, Books}
import org.mockito.Mockito.when
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.FakeRequest
import play.api.test.Helpers._
import org.mockito.ArgumentMatchers._
import play.api.libs.json.{Format, Json}
import uk.co.jemos.podam.api.PodamFactoryImpl

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

/**
  * Created by sfrsebastian on 4/12/17.
  */
class BookControllerTest extends CrudControllerTestTrait[Book, Books, BookController, BookLogic] {

  var logicMock = mock[BookLogic]
  var controller = new BookController(logicMock)

  implicit val format = Json.format[Book]

  override def generatePojo: Book = factory.manufacturePojo(classOf[Book])

  override def beforeEach(){
    logicMock = mock[BookLogic]
    controller = new BookController(logicMock)
  }

  /*"Al crear un nuevo libro" must {
    "Se deberia retornar el mensaje esperado cuando el formato del contenido no corresponde al del recurso" in {
      val request = FakeRequest().withJsonBody(Json.parse("{}"))
      val result = call(controller.create(), request)
      val respuesta = contentAsString(result)
      assert(respuesta == "Error en formato de contenido", "El mensaje deberia ser Error en formato de contenido")
      assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
    }

    "Se deberia retornar el mensaje esperado cuando el elemento no pudo ser creado" in {
      val nuevoLibro = factory.manufacturePojo(classOf[Book])
      val request = FakeRequest().withJsonBody(Json.toJson(nuevoLibro))
      when(logicMock.create(any(classOf[Book]))) thenReturn Future(None)
      val result = call(controller.create(), request)
      val respuesta = contentAsString(result)
      assert(respuesta == "El elemento no pudo ser creado", "El mensaje deberia ser El elemento no pudo ser creado")
      assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
    }

    "Se deberia retornar el elemento creado cuando si pudo ser creado" in {
       val nuevoLibro = factory.manufacturePojo(classOf[Book])
       val jsonLibro = Json.toJson(nuevoLibro)
       val request = FakeRequest().withJsonBody(jsonLibro)
       when(logicMock.create(any(classOf[Book]))) thenReturn Future(Some(nuevoLibro))
       val result = call(controller.create(), request)
       val respuesta = contentAsJson(result)
       assert(respuesta == jsonLibro, "El json recibido debe corresponder al elemento obtenido por la logica")
       assert(status(result) == CREATED, "El codigo de respuesta debe ser 201")
     }
   }

   "Al solicitar un libro" must {
     "Se debe recibir el json del objeto solicitado" in {
       val id = Random.nextInt(20)
       val nuevoLibro = factory.manufacturePojo(classOf[Book])
       val request = FakeRequest()
       val jsonElemento = Json.toJson(nuevoLibro)
       when(logicMock.get(anyInt())) thenReturn Future(Some(nuevoLibro))
       val result = call(controller.get(id), request)
       val jsonRespuesta = contentAsJson(result)
       assert(jsonRespuesta == jsonElemento, "El json recibido debe corresponder al elemento obtenido por la logica")
       assert(status(result) == OK, "El codigo de respuesta debe ser 200")
     }

     "Se debe recibir el mensaje esperado cuando no existe el elemento" in {
       val id = Random.nextInt(20)
       val request = FakeRequest()
       when(logicMock.get(anyInt())) thenReturn Future(None)
       val result = call(controller.get(id), request)
       val respuesta = contentAsString(result)
       assert(respuesta == "El elemento no existe", "El mensaje deberia ser El elemento no existe")
       assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
     }
   }

   "Al solicitar todos los libros" must {
     "Se debe recibir el json de la coleccion solicitada" in {
       val nuevoLibro = (0 to 20).map(_ => factory.manufacturePojo(classOf[Book]))
       val jsonElemento = Json.toJson(nuevoLibro)
       val request = FakeRequest()
       when(logicMock.getAll) thenReturn Future(nuevoLibro)
       val result = call(controller.getAll, request)
       val jsonRespuesta = contentAsJson(result)
       assert(jsonRespuesta == jsonElemento, "El json recibido debe corresponder al elemento obtenido")
       assert(status(result) == OK, "El codigo de respuesta debe ser 200")
     }
   }

   "Al actualizar un libro" must {
     "Se deberia retornar el mensaje esperado cuando el formato del contenido no corresponde al del recurso" in {
       val request = FakeRequest().withJsonBody(Json.parse("{}"))
       val id = Random.nextInt(20)
       val result = call(controller.update(id), request)
       val respuesta = contentAsString(result)
       assert(respuesta == "Error en formato de contenido", "El mensaje deberia ser Error en formato de contenido")
       assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
     }

     "Se deberia retornar el mensaje esperado cuando el elemento no pudo ser actualizado" in {
       val nuevoLibro = factory.manufacturePojo(classOf[Book])
       val id = Random.nextInt(20)
       val request = FakeRequest().withJsonBody(Json.toJson(nuevoLibro))
       when(logicMock.update(anyInt(), any(classOf[Book]))) thenReturn Future(None)
       val result = call(controller.update(id), request)
       val respuesta = contentAsString(result)
       assert(respuesta == "El elemento no pudo ser actualizado", "El mensaje deberia ser El elemento no pudo ser actualizado")
       assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
     }

     "Se deberia retornar el elemento creado cuando si pudo ser actualizado" in {
       val nuevoLibro = factory.manufacturePojo(classOf[Book])
       val jsonElemento = Json.toJson(nuevoLibro)
       val id = Random.nextInt(20)
       val request = FakeRequest().withJsonBody(Json.toJson(nuevoLibro))
       when(logicMock.update(anyInt(), any(classOf[Book]))) thenReturn Future(Some(nuevoLibro))
       val result = call(controller.update(id), request)
       val jsonRespuesta = contentAsJson(result)
       assert(jsonRespuesta == jsonElemento, "El json recibido debe corresponder al elemento obtenido")
       assert(status(result) == OK, "El codigo de respuesta debe ser 200")
     }
   }

   "Al eliminar un libro" must {
     "Se deberia retornar el mensaje esperado cuando el elemento no pudo ser eliminado" in {
       val id = Random.nextInt(20)
       val request = FakeRequest()
       when(logicMock.delete(anyInt())) thenReturn Future(None)
       val result = call(controller.delete(id), request)
       val respuesta = contentAsString(result)
       assert(respuesta == "El elemento con id dado no existe", "El mensaje deberia ser El elemento con id dado no existe")
       assert(status(result) == BAD_REQUEST, "El codigo de respuesta debe ser 400")
     }

     "Se deberia retornar el elemento eliminado cuando la operacion es exitosa" in {
       val nuevoLibro = factory.manufacturePojo(classOf[Book])
       val jsonElemento = Json.toJson(nuevoLibro)
       val id = Random.nextInt(20)
       val request = FakeRequest()
       when(logicMock.delete(anyInt())) thenReturn Future(Some(nuevoLibro))
       val result = call(controller.delete(id), request)
       val jsonRespuesta = contentAsJson(result)
       assert(jsonRespuesta == jsonElemento, "El json recibido debe corresponder al elemento obtenido")
       assert(status(result) == OK, "El codigo de respuesta debe ser 200")
     }
   }*/
}
