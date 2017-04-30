import editorial.models.{Editorial, Editorials}
import editorial.persistence.EditorialPersistence
import crud.layers.CrudPersistence
import crud.tests.CrudPersistenceTestTrait

class EditorialPersistenceTest extends CrudPersistenceTestTrait[Editorial, Editorials]{

  override var persistence: CrudPersistence[Editorial, Editorials] = new EditorialPersistence
  override var seedCollection: Seq[Editorial] = Nil
  override def generatePojo(): Editorial = factory.manufacturePojo(classOf[Editorial])
}