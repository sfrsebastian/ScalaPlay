package traits

import author.logic.AuthorLogic
import author.model._
import author.persistence.AuthorPersistence
import org.mockito.Mockito._
import slick.jdbc.PostgresProfile.api._
import tests.logic.CrudLogicTestTrait

/**
  * Created by sfrsebastian on 4/12/17.
  */
trait AuthorLogicTestTrait extends CrudLogicTestTrait[Author, AuthorPersistenceModel, AuthorTable, AuthorLogic, AuthorPersistence]{

  var persistenceMock = mock[AuthorPersistence]

  var logic = new AuthorLogic(persistenceMock)

  override def beforeEach(){
    persistenceMock = mock[AuthorPersistence]
    when(persistenceMock.table) thenReturn mock[TableQuery[AuthorTable]]
    logic = new AuthorLogic(persistenceMock)
  }

  override def generatePojo: Author = factory.manufacturePojo(classOf[Author])
}
