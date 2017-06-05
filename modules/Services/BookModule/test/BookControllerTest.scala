/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
import traits.BookControllerTestTrait

/*IMPORTS CON AUTENTICACION/AUTORIZACION
import com.google.inject.AbstractModule
import com.mohiva.play.silhouette.api.util.PasswordInfo
import java.util.UUID
import models.user.User
import auth.settings.AuthenticationEnvironment
import com.mohiva.play.silhouette.api.{Environment, LoginInfo}
import com.mohiva.play.silhouette.test._
import net.codingwell.scalaguice.ScalaModule
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.when
import play.api.test.FakeRequest
import play.api.test.Helpers.{call, contentAsJson, status}
import play.api.test.Helpers._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future*/

class BookControllerTest extends BookControllerTestTrait{

  /*
  override lazy val app = new GuiceApplicationBuilder()
   .overrides(bind[AuthorLogicTrait].toInstance(logicMock))
   .overrides(new FakeModule())
   .build

  class FakeModule extends AbstractModule with ScalaModule {
     def configure() = {
       bind[Environment[AuthenticationEnvironment]].toInstance(env)
     }
  }

 val identity = User(1,UUID.randomUUID(),"John","Doe", true, "john@gmail.com", LoginInfo("credentials", "john@gmail.com"),PasswordInfo("","", None), Array("admin"))

 implicit val env = new FakeEnvironment[AuthenticationEnvironment](Seq(identity.loginInfo -> identity))

 override def getAllTest: Unit = {
   "Se debe recibir el json de la coleccion solicitada" in {
     val newResource = (0 to 20).map(_ => generatePojo)
     val jsonResource = Json.toJson(newResource)
     val request = FakeRequest().withAuthenticator(identity.loginInfo)
     when(logicMock.getAll(anyInt(), anyInt())) thenReturn Future(newResource)
     val result = call(controller.getAll(None, None),request)
     val jsonResponse = contentAsJson(result)
     assert(jsonResponse == jsonResource, "El json recibido debe corresponder al recurso obtenido")
     assert(status(result) == OK, "El codigo de respuesta debe ser 200")
   }
 }*/
}
