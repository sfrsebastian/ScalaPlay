import author.model.{AuthorPersistenceModel, AuthorTable}
import author.persistence.AuthorPersistence
import crud.layers.CrudPersistence
import crud.tests.CrudPersistenceTestTrait

/**
  * Created by sfrsebastian on 5/2/17.
  */
trait AuthorPersistenceTestTrait extends CrudPersistenceTestTrait[AuthorPersistenceModel, AuthorTable]{
  override var persistence: CrudPersistence[AuthorPersistenceModel, AuthorTable] = new AuthorPersistence
  override var seedCollection: Seq[AuthorPersistenceModel] = Nil
  override def generatePojo(): AuthorPersistenceModel = factory.manufacturePojo(classOf[AuthorPersistenceModel])
}
