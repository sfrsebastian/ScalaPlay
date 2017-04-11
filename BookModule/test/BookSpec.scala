import persistence.bookModule.{BookPersistenceTest, BookPersistenceTrait}
import play.api.inject.guice.GuiceApplicationBuilder
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.inject.bind

class BookSpec extends PlaySpec with GuiceOneAppPerSuite {
  override lazy val app = new GuiceApplicationBuilder()
    .overrides(bind[BookPersistenceTrait].to[BookPersistenceTest])
    .build

  val persistence = app.injector.instanceOf[BookPersistenceTrait]

  "BookController" should {
    "Get All" should {
      "debe retornar una coleccion vacia" in {
        val result = persistence.getAll.map(response => {
          println("response is")
          println(response)
          response.length mustBe 0
        })
      }
    }
  }
}