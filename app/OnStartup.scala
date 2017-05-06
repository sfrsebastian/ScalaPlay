package settings

import com.google.inject.Inject
import auth.models.user.{UserPersistenceModel, UserTable}
import author.model.{AuthorPersistenceModel, AuthorTable}
import authorbook.model.{AuthorBookPersistenceModel, AuthorBookTable}
import crud.DatabaseOperations
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._
import comment.model.{CommentPersistenceModel, CommentTable}
import book.model.{BookPersistenceModel, BookTable}
import editorial.model.{EditorialPersistenceModel, EditorialTable}
import play.api.Configuration
import uk.co.jemos.podam.api.PodamFactoryImpl

import scala.util.Random

class OnStartup @Inject()(configuration:Configuration) {
  val factory = new PodamFactoryImpl
  val db = Database.forConfig("Database")
  val books = TableQuery[BookTable]
  val users = TableQuery[UserTable]
  val comments = TableQuery[CommentTable]
  val authors = TableQuery[AuthorTable]
  val authorBook = TableQuery[AuthorBookTable]
  val editorials = TableQuery[EditorialTable]

  if(configuration.getBoolean("dropCreate").getOrElse(false)){
    DatabaseOperations.DropCreate[UserPersistenceModel, UserTable](db, users)
    DatabaseOperations.DropCreate[AuthorBookPersistenceModel, AuthorBookTable](db, authorBook)
    DatabaseOperations.DropCreate[CommentPersistenceModel, CommentTable](db, comments)
    DatabaseOperations.DropCreate[BookPersistenceModel, BookTable](db, books)
    DatabaseOperations.DropCreate[AuthorPersistenceModel, AuthorTable](db, authors)
    DatabaseOperations.DropCreate[EditorialPersistenceModel, EditorialTable](db, editorials)
  }

  DatabaseOperations.createIfNotExist[UserPersistenceModel, UserTable](db, users)
  DatabaseOperations.createIfNotExist[EditorialPersistenceModel, EditorialTable](db, editorials)
  DatabaseOperations.createIfNotExist[BookPersistenceModel, BookTable](db, books)
  DatabaseOperations.createIfNotExist[AuthorPersistenceModel, AuthorTable](db, authors)
  DatabaseOperations.createIfNotExist[CommentPersistenceModel, CommentTable](db, comments)
  DatabaseOperations.createIfNotExist[AuthorBookPersistenceModel, AuthorBookTable](db, authorBook)

  if(configuration.getBoolean("seed").getOrElse(false)){
    val seedEditorials = for {
      _ <- 0 to 19
    }yield {
      val editorial = factory.manufacturePojo(classOf[EditorialPersistenceModel])
      editorial
    }
    val action1 = editorials ++= seedEditorials

    val seedBooks = for {
      _ <- 0 to 19
    }yield factory.manufacturePojo(classOf[BookPersistenceModel]).copy(editorialId = Random.nextInt(20) + 1)
    val action2 = books ++= seedBooks

    val seedAuthors = for {
      _ <- 0 to 19
    }yield factory.manufacturePojo(classOf[AuthorPersistenceModel])
    val action3 = authors ++= seedAuthors

    val seedAuthorBook = for {
      _ <- 0 to 19
    }yield factory.manufacturePojo(classOf[AuthorBookPersistenceModel]).copy(bookId = (Random.nextInt(20) + 1), authorId = (Random.nextInt(20) + 1))
    val action4 = authorBook ++= seedAuthorBook

    val seedComments = for {
      _ <- 0 to 100
    }yield {
      val comment = factory.manufacturePojo(classOf[CommentPersistenceModel])
      comment.copy(bookId = (Random.nextInt(20) + 1))
    }
    val action5 = comments ++= seedComments

    db.run(DBIO.seq(action1, action2, action3, action4, action5))
  }
}

