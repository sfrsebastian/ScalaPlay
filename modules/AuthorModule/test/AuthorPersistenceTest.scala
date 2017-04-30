import author.models.{Author, Authors}
import author.persistence.AuthorPersistence
import crud.layers.CrudPersistence
import crud.tests.CrudPersistenceTestTrait

class AuthorPersistenceTest extends CrudPersistenceTestTrait[Author, Authors]{

  override var persistence: CrudPersistence[Author, Authors] = new AuthorPersistence
  override var seedCollection: Seq[Author] = Nil
  override def generatePojo(): Author = factory.manufacturePojo(classOf[Author])
}