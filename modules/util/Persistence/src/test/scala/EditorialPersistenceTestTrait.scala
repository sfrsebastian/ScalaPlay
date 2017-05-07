import author.model.AuthorTable
import authorbook.model.AuthorBookTable
import book.model.BookTable
import comment.model.CommentTable
import crud.tests.CrudPersistenceTestTrait
import editorial.model._
import editorial.persistence.EditorialPersistence
import slick.lifted.TableQuery

/**
  * Created by sfrsebastian on 5/6/17.
  */
trait EditorialPersistenceTestTrait extends CrudPersistenceTestTrait[Editorial, EditorialPersistenceModel, EditorialTable]{
  override val tables = Seq(
    TableQuery[EditorialTable],
    TableQuery[BookTable],
    TableQuery[AuthorTable],
    TableQuery[CommentTable],
    TableQuery[AuthorBookTable]
  )
  override val persistence = new EditorialPersistence
  override var seedCollection: Seq[Editorial] = Nil
  override def generatePojo(): Editorial = factory.manufacturePojo(classOf[Editorial])
  override implicit def Persistence2Model = EditorialPersistenceConverter
}
