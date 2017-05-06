import author.model.{AuthorPersistenceModel, AuthorTable}
import authorbook.model.{AuthorBookPersistenceModel, AuthorBookTable}
import book.model._
import book.persistence.BookPersistence
import comment.model.{CommentPersistenceModel, CommentTable}
import comment.persistence.CommentPersistence
import crud.DatabaseOperations
import crud.tests.CrudPersistenceTestTrait
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.util.Random
import scala.concurrent.duration._

trait BookPersistenceTestTrait extends CrudPersistenceTestTrait[Book, BookPersistenceModel, BookTable]{
  var authorTable = TableQuery[AuthorTable]
  var authorCollection:Seq[AuthorPersistenceModel] = Nil
  def generateAuthorPojo() = factory.manufacturePojo(classOf[AuthorPersistenceModel])

  var authorBookTable = TableQuery[AuthorBookTable]
  var authorBookCollection:Seq[AuthorBookPersistenceModel] = Nil
  def generateAuthorBookPojo() = factory.manufacturePojo(classOf[AuthorBookPersistenceModel]).copy(bookId = (Random.nextInt(19) + 1), authorId = (Random.nextInt(19) + 1))

  var commentCollection:Seq[CommentPersistenceModel] = Nil
  def generateCommentPojo() = factory.manufacturePojo(classOf[CommentPersistenceModel]).copy(bookId = Random.nextInt(19) + 1)

  override val persistence = new BookPersistence(new CommentPersistence)
  override var seedCollection: Seq[BookPersistenceModel] = Nil
  override def generatePojo: BookPersistenceModel = factory.manufacturePojo(classOf[BookPersistenceModel])
  override implicit def Model2Persistence = BookPersistenceConverter
  override implicit def Persistence2Model = PersistenceBookConverter

  override def beforeAll(): Unit = {
    super.beforeAll()
    Thread.sleep(1000)
    DatabaseOperations.createIfNotExist[AuthorPersistenceModel,AuthorTable](persistence.db, authorTable)
    Thread.sleep(1000)
    DatabaseOperations.createIfNotExist[CommentPersistenceModel,CommentTable](persistence.db, persistence.commentPersistence.table)
    Thread.sleep(1000)
    DatabaseOperations.createIfNotExist[AuthorBookPersistenceModel, AuthorBookTable](persistence.db, authorBookTable)
  }

  override def beforeEach(){
    DatabaseOperations.Drop[CommentPersistenceModel,CommentTable](persistence.db, persistence.commentPersistence.table)
    Thread.sleep(1000)
    DatabaseOperations.Drop[AuthorBookPersistenceModel,AuthorBookTable](persistence.db, authorBookTable)
    Thread.sleep(1000)
    DatabaseOperations.Drop[BookPersistenceModel, BookTable](persistence.db, persistence.table)
    Thread.sleep(1000)
    DatabaseOperations.createIfNotExist[BookPersistenceModel, BookTable](persistence.db, persistence.table)
    Thread.sleep(1000)
    super.beforeEach()
    Thread.sleep(1000)
    DatabaseOperations.DropCreate[AuthorPersistenceModel, AuthorTable](persistence.db, authorTable)
    Thread.sleep(1000)
    DatabaseOperations.createIfNotExist[CommentPersistenceModel,CommentTable](persistence.db, persistence.commentPersistence.table)
    Thread.sleep(1000)
    DatabaseOperations.createIfNotExist[AuthorBookPersistenceModel,AuthorBookTable](persistence.db, authorBookTable)
    Thread.sleep(1000)
    commentCollection = Seq()
    for(_ <- 0 to 19){
      val pojo = generateCommentPojo
      commentCollection = commentCollection :+ pojo
    }
    Await.result(persistence.db.run(persistence.commentPersistence.table ++= commentCollection), 10.second)
    Thread.sleep(1000)
    authorCollection = Seq()
    for(_ <- 0 to 19){
      val pojo = generateAuthorPojo
      authorCollection = authorCollection :+ pojo
    }
    Await.result(persistence.db.run(authorTable ++= authorCollection), 10.second)
    Thread.sleep(1000)
    authorBookCollection = Seq()
    for(_ <- 0 to 19){
      val pojo = generateAuthorBookPojo
      authorBookCollection = authorBookCollection :+ pojo
    }
    Await.result(persistence.db.run(authorBookTable ++= authorBookCollection), 10.second)
  }

  override def afterAll():Unit = {
    DatabaseOperations.Drop[CommentPersistenceModel,CommentTable](persistence.db, persistence.commentPersistence.table)
    Thread.sleep(1000)
    DatabaseOperations.Drop[AuthorBookPersistenceModel,AuthorBookTable](persistence.db, authorBookTable)
    Thread.sleep(1000)
    DatabaseOperations.Drop[AuthorPersistenceModel, AuthorTable](persistence.db, authorTable)
    Thread.sleep(1000)
    super.afterAll()
  }

  override def assertByProperties(e1: BookPersistenceModel, e2: BookPersistenceModel): Unit = {
    super.assertByProperties(e1, e2)
    assert(e1.description == e2.description, "La descripcion deberia ser la misma")
    assert(e1.ISBN == e2.ISBN, "El ISBN deberia ser el mismo")
    assert(e1.image == e2.image, "La imagen deberia ser la misma")
  }
}