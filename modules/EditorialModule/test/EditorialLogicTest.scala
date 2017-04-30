import editorial.logic.EditorialLogic
import editorial.models.{Editorial, Editorials}
import editorial.persistence.EditorialPersistence
import crud.tests.CrudLogicTestTrait
import org.mockito.Mockito._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/12/17.
  */
class EditorialLogicTest extends CrudLogicTestTrait[Editorial, Editorials, EditorialLogic, EditorialPersistence]{

  var persistenceMock = mock[EditorialPersistence]
  var logic = new EditorialLogic(persistenceMock)

  override def beforeEach(){
    persistenceMock = mock[EditorialPersistence]
    when(persistenceMock.table) thenReturn mock[TableQuery[Editorials]]
    logic = new EditorialLogic(persistenceMock)
  }

  override def generatePojo: Editorial = factory.manufacturePojo(classOf[Editorial])
}
