import crud.tests.CrudLogicTestTrait
import editorial.logic.EditorialLogic
import editorial.model.{Editorial, EditorialPersistenceModel, EditorialTable}
import editorial.persistence.EditorialPersistence
import org.mockito.Mockito.when
import slick.lifted.TableQuery

/**
  * Created by sfrsebastian on 5/6/17.
  */
trait EditorialLogicTestTrait extends CrudLogicTestTrait[Editorial, EditorialPersistenceModel, EditorialTable, EditorialLogic, EditorialPersistence]{

  var persistenceMock = mock[EditorialPersistence]
  var logic = new EditorialLogic(persistenceMock)

  override def beforeEach(){
    persistenceMock = mock[EditorialPersistence]
    when(persistenceMock.table) thenReturn mock[TableQuery[EditorialTable]]
    logic = new EditorialLogic(persistenceMock)
  }

  override def generatePojo: Editorial = factory.manufacturePojo(classOf[Editorial])
}
