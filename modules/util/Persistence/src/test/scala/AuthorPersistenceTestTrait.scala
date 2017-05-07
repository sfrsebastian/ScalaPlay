import author.model._
import author.persistence.AuthorPersistence
import book.persistence.BookPersistence
import comment.persistence.CommentPersistence
import crud.layers.CrudPersistence
import crud.tests.CrudPersistenceTestTrait
import slick.lifted.TableQuery

/**
  * Created by sfrsebastian on 5/2/17.
  */
trait AuthorPersistenceTestTrait extends CrudPersistenceTestTrait[Author, AuthorPersistenceModel, AuthorTable]{

  override val tables = Seq(
    TableQuery[AuthorTable]
  )
  override val persistence =  new AuthorPersistence(new BookPersistence(new CommentPersistence))
  override var seedCollection: Seq[Author] = Nil
  override def generatePojo(): Author = factory.manufacturePojo(classOf[Author])
  override implicit def Persistence2Model = AuthorPersistenceConverter
}
