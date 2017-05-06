import book.model.{BookPersistenceModel, BookTable}
import book.persistence.BookPersistence
import comment.model._
import comment.persistence.CommentPersistence
import crud.DatabaseOperations
import crud.tests.CrudPersistenceTestTrait
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Random

trait CommentPersistenceTestTrait extends CrudPersistenceTestTrait[Comment, CommentPersistenceModel, CommentTable]{
  val bookPersistence = new BookPersistence(persistence)
  var bookCollection:Seq[BookPersistenceModel] = Nil
  val generateBookPojo = factory.manufacturePojo(classOf[BookPersistenceModel]).copy(Random.nextInt(19) + 1)

  override val persistence = new CommentPersistence
  override var seedCollection: Seq[CommentPersistenceModel] = Nil
  override def generatePojo : CommentPersistenceModel = factory.manufacturePojo(classOf[CommentPersistenceModel]).copy(bookId = Random.nextInt(19) + 1)
  override implicit def Model2Persistence = CommentPersistenceConverter
  override implicit def Persistence2Model = PersistenceCommentConverter

  override def beforeAll() {
    DatabaseOperations.createIfNotExist[BookPersistenceModel,BookTable](bookPersistence.db, bookPersistence.table)
    Thread.sleep(1000)
    super.beforeAll()
  }

  override def beforeEach(){
    bookCollection = Seq()
    DatabaseOperations.DropCreate[BookPersistenceModel,BookTable](bookPersistence.db, bookPersistence.table)
    Thread.sleep(1000)
    for(_ <- 0 to 19){
      val pojo = generateBookPojo
      bookCollection = bookCollection :+ pojo
    }
    Await.result(bookPersistence.db.run(bookPersistence.table ++= bookCollection), 10.second)
    Thread.sleep(1000)
    super.beforeEach()
  }

  override def afterAll() {
    super.afterAll()
    Thread.sleep(1000)
    DatabaseOperations.Drop[BookPersistenceModel,BookTable](bookPersistence.db, bookPersistence.table)
  }
}