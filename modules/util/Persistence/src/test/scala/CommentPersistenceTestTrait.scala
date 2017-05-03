import comment.model._
import comment.persistence.CommentPersistence
import crud.DatabaseOperations
import crud.layers.CrudPersistence
import crud.tests.CrudPersistenceTestTrait
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Random

trait CommentPersistenceTestTrait extends CrudPersistenceTestTrait[CommentPersistenceModel, CommentTable]{
  override var persistence: CrudPersistence[CommentPersistenceModel, CommentTable] = new CommentPersistence
  var bookPersistenceTest = new BookPersistenceTest
  override var seedCollection: Seq[CommentPersistenceModel] = Nil
  override def generatePojo(): CommentPersistenceModel = factory.manufacturePojo(classOf[CommentPersistenceModel]).copy(bookId = Random.nextInt(19) + 1)

  override def beforeAll(): Unit = {
    bookPersistenceTest.beforeAll
    Thread.sleep(1000)
    DatabaseOperations.createIfNotExist[CommentPersistenceModel,CommentTable](persistence.db, persistence.table)
    Thread.sleep(1000)
  }

  override def beforeEach(){
    bookPersistenceTest.beforeEach
    Thread.sleep(1000)
    seedCollection = Seq()
    DatabaseOperations.DropCreate[CommentPersistenceModel,CommentTable](persistence.db, persistence.table)
    Thread.sleep(1000)
    for(_ <- 1 to 20){
      val pojo = generatePojo
      seedCollection = seedCollection :+ pojo
      Await.result(persistence.db.run(persistence.table += pojo), 10.second)
    }
  }

  override def afterAll():Unit = {
    DatabaseOperations.Drop[CommentPersistenceModel,CommentTable](persistence.db, persistence.table)
    bookPersistenceTest.afterAll
  }
}