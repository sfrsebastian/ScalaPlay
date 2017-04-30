
import author.logic.{AuthorLogic, AuthorLogicTrait}
import author.models.{Author, Authors}
import controllers.author.AuthorController
import crud.tests.CrudControllerTestTrait
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json

/*IMPORTS CON AUTENTICACION/AUTORIZACION
import com.google.inject.AbstractModule
import com.mohiva.play.silhouette.api.util.PasswordInfo
import java.util.UUID
import auth.models.User
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

/**
  * Created by sfrsebastian on 4/12/17.
  */

class AuthorControllerTest extends CrudControllerTestTrait[Author, Authors, AuthorController, AuthorLogic] {

  var logicMock = mock[AuthorLogic]

  var controller = app.injector.instanceOf[AuthorController]

  implicit val format = Json.format[Author]

  override def generatePojo: Author = factory.manufacturePojo(classOf[Author])

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[AuthorLogicTrait].toInstance(logicMock))
    //.overrides(new FakeModule())
    .build


 /* class FakeModule extends AbstractModule with ScalaModule {
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
