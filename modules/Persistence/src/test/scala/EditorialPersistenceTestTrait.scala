import crud.tests.CrudPersistenceTestTrait
import editorial.model._
import editorial.persistence.EditorialPersistence
import persistence.DatabasePopulator

/**
  * Created by sfrsebastian on 5/6/17.
  */
trait EditorialPersistenceTestTrait extends CrudPersistenceTestTrait[Editorial, EditorialPersistenceModel, EditorialTable]{
  override val tables = DatabasePopulator.tables

  override val persistence = new EditorialPersistence

  override var seedCollection: Seq[Editorial] = Seq()

  override def generatePojo(): Editorial = DatabasePopulator.generateEditorial(1)

  override implicit def Persistence2Model = EditorialPersistenceConverter

  override def populateDatabase = {
    val populate = DatabasePopulator.populate
    seedCollection = DatabasePopulator.editorials
    populate
  }

  override def assertByProperties(e1: EditorialPersistenceModel, e2: EditorialPersistenceModel, compareRef:Boolean = true): Unit = {
    super.assertByProperties(e1, e2)
    assert(e1.address == e2.address, "La direccion deberia ser la misma")
  }
}
