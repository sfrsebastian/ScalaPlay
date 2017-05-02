import author.persistence.AuthorPersistence
import crud.layers.CrudPersistence
import crud.tests.CrudPersistenceTestTrait
import models.author.{AuthorPersistenceModel, AuthorTable}

class AuthorPersistenceTest extends CrudPersistenceTestTrait[AuthorPersistenceModel, AuthorTable]{

  override var persistence: CrudPersistence[AuthorPersistenceModel, AuthorTable] = new AuthorPersistence
  override var seedCollection: Seq[AuthorPersistenceModel] = Nil
  override def generatePojo(): AuthorPersistenceModel = factory.manufacturePojo(classOf[AuthorPersistenceModel])
}