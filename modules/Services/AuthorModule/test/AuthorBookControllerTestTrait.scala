import akka.stream.Materializer
import author.model.{Author, AuthorMin}
import book.logic.{BookLogic, BookLogicTrait}
import book.model.{Book, BookDetail, BookDetailConverter, BookMin}
import comment.model.CommentMin
import controllers.author.AuthorBookController
import crud.models.ModelConverter
import editorial.model.EditorialMin
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers.{call, contentAsString, status}
import uk.co.jemos.podam.api.PodamFactoryImpl
import play.api.inject.bind
import play.api.test.Helpers._

import scala.concurrent.Future

/**
  * Created by sfrsebastian on 5/29/17.
  */
trait AuthorBookControllerTestTrait extends PlaySpec with GuiceOneAppPerSuite with ScalaFutures with MockitoSugar{

  val factory = new PodamFactoryImpl

  implicit lazy val materializer: Materializer = app.materializer

  var logicMock = mock[BookLogic]

  var controller = app.injector.instanceOf[AuthorBookController]

  implicit val commentMinFormat = Json.format[CommentMin]

  implicit val authorMin = Json.format[AuthorMin]

  implicit val editorialMin = Json.format[EditorialMin]

  implicit val formatDetail = Json.format[BookDetail]

  def generatePojo: Book = {
    val author = factory.manufacturePojo(classOf[Author]).copy(books = Seq())
    factory.manufacturePojo(classOf[Book]).copy(authors = Seq(author), comments = Seq())
  }

  implicit def Model2Detail = BookDetailConverter

  implicit def M2S (f : BookMin)(implicit converter : ModelConverter[Book,BookMin]) : Book = converter.convertInverse(f)

  implicit def S2M (s: Book)(implicit converter : ModelConverter[Book,BookMin]):BookMin = converter.convert(s)

  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[BookLogicTrait].toInstance(logicMock))
    .build

  "Al solicitar los libros de un autor" must {
    "Se deberia retornar un listado de sus libros" in {
      val newResource = (0 to 20).map(_ => generatePojo)
      val jsonResource = Json.toJson(newResource.map(e=>e:BookMin))
      val request = FakeRequest()
      when(logicMock.getAll(anyInt(), anyInt())) thenReturn Future(newResource)
      val result = call(controller.getBooksFromAuthor(anyInt()), request)
      val jsonResponse = contentAsJson(result)
      assert(jsonResponse == jsonResource, "El json recibido debe corresponder al recurso obtenido")
      assert(status(result) == OK, "El codigo de respuesta debe ser 200")
    }
  }
}
