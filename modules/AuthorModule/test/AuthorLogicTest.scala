import author.logic.AuthorLogic
import author.models.{Author, Authors}
import author.persistence.AuthorPersistence
import crud.tests.CrudLogicTestTrait
import org.mockito.Mockito._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/12/17.
  */
class AuthorLogicTest extends CrudLogicTestTrait[Author, Authors, AuthorLogic, AuthorPersistence]{

  var persistenceMock = mock[AuthorPersistence]
  var logic = new AuthorLogic(persistenceMock)

  override def beforeEach(){
    persistenceMock = mock[AuthorPersistence]
    when(persistenceMock.table) thenReturn mock[TableQuery[Authors]]
    logic = new AuthorLogic(persistenceMock)
  }

  override def generatePojo: Author = factory.manufacturePojo(classOf[Author])
}
