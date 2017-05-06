package settings

import com.google.inject.Inject
import auth.models.user.{UserPersistenceModel, UserTable}
import crud.DatabaseOperations
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._
import comment.model.{CommentPersistenceModel, CommentTable}
import book.model.{BookPersistenceModel, BookTable}
import play.api.Configuration
import uk.co.jemos.podam.api.PodamFactoryImpl

import scala.util.Random

class OnStartup @Inject()(configuration:Configuration) {
  val factory = new PodamFactoryImpl
  val db = Database.forConfig("Database")
  val books = TableQuery[BookTable]
  val users = TableQuery[UserTable]
  val comments = TableQuery[CommentTable]
  /*
  val authors = TableQuery[Authors]
  val editorials = TableQuery[Editorials]*/

  if(configuration.getBoolean("dropCreate").getOrElse(false)){
    DatabaseOperations.DropCreate[UserPersistenceModel, UserTable](db, users)
    DatabaseOperations.DropCreate[BookPersistenceModel, BookTable](db, books)
    DatabaseOperations.DropCreate[CommentPersistenceModel, CommentTable](db, comments)
    /*DatabaseOperations.DropCreate[Author, Authors](db, authors)
    DatabaseOperations.DropCreate[Editorial, Editorials](db, editorials)*/
  }

  DatabaseOperations.createIfNotExist[UserPersistenceModel, UserTable](db, users)
  DatabaseOperations.createIfNotExist[BookPersistenceModel, BookTable](db, books)
  DatabaseOperations.createIfNotExist[CommentPersistenceModel, CommentTable](db, comments)
/*  DatabaseOperations.createIfNotExist[Author, Authors](db, authors)
  DatabaseOperations.createIfNotExist[Editorial, Editorials](db, editorials)*/

  if(configuration.getBoolean("seed").getOrElse(false)){
    val seedCollection = for {
      _ <- 0 to 19
    }yield factory.manufacturePojo(classOf[BookPersistenceModel])
    val action1 = books ++= seedCollection

    val seedComments = for {
      _ <- 0 to 100
    }yield {
      val comment = factory.manufacturePojo(classOf[CommentPersistenceModel])
      comment.copy(bookId = (Random.nextInt(20) + 1))
    }
    val action2 = comments ++= seedComments
    db.run(DBIO.seq(action1,action2))
  }
}

