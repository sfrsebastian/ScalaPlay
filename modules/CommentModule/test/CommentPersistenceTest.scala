import models.comment._
import comment.persistence.CommentPersistence
import crud.layers.CrudPersistence
import crud.tests.CrudPersistenceTestTrait

class CommentPersistenceTest extends CrudPersistenceTestTrait[CommentPersistenceModel, CommentTable]{
  override var persistence: CrudPersistence[CommentPersistenceModel, CommentTable] = new CommentPersistence
  override var seedCollection: Seq[CommentPersistenceModel] = Nil
  override def generatePojo(): CommentPersistenceModel = factory.manufacturePojo(classOf[CommentPersistenceModel])
}