import crud.tests.CrudPersistenceTestTrait
import editorial.model._
import editorial.persistence.EditorialPersistence

/**
  * Created by sfrsebastian on 5/6/17.
  */
trait EditorialPersistenceTestTrait extends CrudPersistenceTestTrait[Editorial, EditorialPersistenceModel, EditorialTable]{
  override val persistence = new EditorialPersistence
  override var seedCollection: Seq[Editorial] = Nil
  override def generatePojo(): Editorial = factory.manufacturePojo(classOf[Editorial])
  override implicit def Model2Persistence = EditorialPersistenceConverter
  override implicit def Persistence2Model = PersistenceEditorialConverter
}
