package settings

import com.google.inject.Inject
import auth.models.user.UserTable
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

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.util.Random

class OnStartup @Inject()(configuration:Configuration) {
  val factory = new PodamFactoryImpl

  implicit  val db = Database.forConfig("Database")

  val tables = Seq(
    TableQuery[UserTable],
    TableQuery[EditorialTable],
    TableQuery[BookTable],
    TableQuery[AuthorTable],
    TableQuery[CommentTable],
    TableQuery[AuthorBookTable]
  )

  if(configuration.getBoolean("dropCreate").getOrElse(false)){
    val dropSequence = db.run(DBIO.sequence(tables.reverse.flatMap(table => DatabaseOperations.Drop(db, table))))
    Await.result(dropSequence, 10.second)
  }

  val createSequence = db.run(DBIO.sequence(tables.flatMap(table => DatabaseOperations.createIfNotExist(db, table))))
  Await.result(createSequence, 10.second)

  if(configuration.getBoolean("seed").getOrElse(false)){
    val seedEditorials = for {
      _ <- 0 to 19
    }yield {
      val editorial = factory.manufacturePojo(classOf[EditorialPersistenceModel])
      editorial
    }
    val action1 = TableQuery[EditorialTable] ++= seedEditorials

    val seedBooks = for {
      _ <- 0 to 19
    }yield factory.manufacturePojo(classOf[BookPersistenceModel]).copy(editorialId = Some(Random.nextInt(20) + 1))
    val action2 = TableQuery[BookTable] ++= seedBooks

    val seedAuthors = for {
      _ <- 0 to 19
    }yield factory.manufacturePojo(classOf[AuthorPersistenceModel])
    val action3 = TableQuery[AuthorTable] ++= seedAuthors

    val seedAuthorBook = for {
      _ <- 0 to 19
    }yield factory.manufacturePojo(classOf[AuthorBookPersistenceModel]).copy(bookId = (Random.nextInt(20) + 1), authorId = (Random.nextInt(20) + 1))
    val action4 = TableQuery[AuthorBookTable] ++= seedAuthorBook

    val seedComments = for {
      _ <- 0 to 100
    }yield {
      val comment = factory.manufacturePojo(classOf[CommentPersistenceModel])
      comment.copy(bookId = (Random.nextInt(20) + 1))
    }
    val action5 = TableQuery[CommentTable] ++= seedComments

    db.run(DBIO.seq(action1, action2, action3, action4, action5))
  }
}