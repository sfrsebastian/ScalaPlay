import author.model._
import author.persistence.AuthorPersistence
import crud.tests.CrudPersistenceTestTrait

/**
  * Created by sfrsebastian on 5/2/17.
  */
trait AuthorPersistenceTestTrait extends CrudPersistenceTestTrait[Author, AuthorPersistenceModel, AuthorTable]{
  override val persistence = new AuthorPersistence
  override var seedCollection: Seq[AuthorPersistenceModel] = Nil
  override def generatePojo(): AuthorPersistenceModel = factory.manufacturePojo(classOf[AuthorPersistenceModel])
  override implicit def Model2Persistence = AuthorPersistenceConverter
  override implicit def Persistence2Model = PersistenceAuthorConverter
}
