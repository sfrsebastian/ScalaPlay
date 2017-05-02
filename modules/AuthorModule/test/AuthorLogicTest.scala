import author.logic.AuthorLogic
import models.author._
import author.persistence.AuthorPersistence
import crud.tests.CrudLogicTestTrait
import org.mockito.Mockito._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/12/17.
  */
class AuthorLogicTest extends CrudLogicTestTrait[Author, AuthorPersistenceModel, AuthorTable, AuthorLogic, AuthorPersistence]{

  var persistenceMock = mock[AuthorPersistence]

  var logic = new AuthorLogic(persistenceMock)

  override implicit def Model2Persistence = AuthorPersistenceConverter

  override implicit def Persistence2Model = PersistenceAuthorConverter

  override def beforeEach(){
    persistenceMock = mock[AuthorPersistence]
    when(persistenceMock.table) thenReturn mock[TableQuery[AuthorTable]]
    logic = new AuthorLogic(persistenceMock)
  }

  override def generatePojo: AuthorPersistenceModel = factory.manufacturePojo(classOf[AuthorPersistenceModel])
}
