package settings

import com.google.inject.Inject
import auth.models.{User, Users}
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
  val users = TableQuery[Users]
  val comments = TableQuery[CommentTable]
  /*
  val authors = TableQuery[Authors]
  val editorials = TableQuery[Editorials]*/

  if(configuration.getBoolean("dropCreate").getOrElse(false)){
    DatabaseOperations.DropCreate[User, Users](db, users)
    DatabaseOperations.DropCreate[BookPersistenceModel, BookTable](db, books)
    DatabaseOperations.DropCreate[CommentPersistenceModel, CommentTable](db, comments)
    /*DatabaseOperations.DropCreate[Author, Authors](db, authors)
    DatabaseOperations.DropCreate[Editorial, Editorials](db, editorials)*/
  }

  DatabaseOperations.createIfNotExist[User, Users](db, users)
  DatabaseOperations.createIfNotExist[BookPersistenceModel, BookTable](db, books)
  DatabaseOperations.createIfNotExist[CommentPersistenceModel, CommentTable](db, comments)
/*  DatabaseOperations.createIfNotExist[Author, Authors](db, authors)
  DatabaseOperations.createIfNotExist[Editorial, Editorials](db, editorials)*/

  if(configuration.getBoolean("seed").getOrElse(false)){
    val seedCollection = for {
      _ <- 0 to 19
    }yield factory.manufacturePojo(classOf[BookPersistenceModel])
    db.run(books ++= seedCollection)

    val seedComments = for {
      _ <- 0 to 100
    }yield {
      val comment = factory.manufacturePojo(classOf[CommentPersistenceModel])
      comment.copy(bookId = (Random.nextInt(20) + 1))
    }
    db.run(comments ++= seedComments)
  }
}

