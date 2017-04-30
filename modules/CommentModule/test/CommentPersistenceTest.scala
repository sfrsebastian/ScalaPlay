import comment.models.{Comment, Comments}
import comment.persistence.CommentPersistence
import crud.layers.CrudPersistence
import crud.tests.CrudPersistenceTestTrait

class CommentPersistenceTest extends CrudPersistenceTestTrait[Comment, Comments]{

  override var persistence: CrudPersistence[Comment, Comments] = new CommentPersistence
  override var seedCollection: Seq[Comment] = Nil
  override def generatePojo(): Comment = factory.manufacturePojo(classOf[Comment])
}