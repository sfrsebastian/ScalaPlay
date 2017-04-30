package settings

import com.google.inject.Inject
import auth.models.{User, Users}
import author.models.{Author, Authors}
import crud.DatabaseOperations
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._
import book.models.{Book, Books}
import comment.models.{Comment, Comments}
import editorial.models.{Editorial, Editorials}
import play.api.Configuration
import uk.co.jemos.podam.api.PodamFactoryImpl

public class OnStartup @Inject()(configuration:Configuration) {
  val factory = new PodamFactoryImpl
  val db = Database.forConfig("Database")
  val books = TableQuery[Books]
  val users = TableQuery[Users]
  val authors = TableQuery[Authors]
  val comments = TableQuery[Comments]
  val editorials = TableQuery[Editorials]

  if(configuration.getBoolean("dropCreate").getOrElse(false)){
    DatabaseOperations.DropCreate[User, Users](db, users)
    DatabaseOperations.DropCreate[Book, Books](db, books)
    DatabaseOperations.DropCreate[Author, Authors](db, authors)
    DatabaseOperations.DropCreate[Comment, Comments](db, comments)
    DatabaseOperations.DropCreate[Editorial, Editorials](db, editorials)
  }

  DatabaseOperations.createIfNotExist[User, Users](db, users)
  DatabaseOperations.createIfNotExist[Book, Books](db, books)
  DatabaseOperations.createIfNotExist[Author, Authors](db, authors)
  DatabaseOperations.createIfNotExist[Comment, Comments](db, comments)
  DatabaseOperations.createIfNotExist[Editorial, Editorials](db, editorials)

  if(configuration.getBoolean("seed").getOrElse(false)){
    val seedCollection = for {
      _ <- 0 to 19
    }yield factory.manufacturePojo(classOf[Book])
    db.run(books ++= seedCollection)

    val seedAuthors = for {
      _ <- 0 to 19
    }yield factory.manufacturePojo(classOf[Author])
    db.run(authors ++= seedAuthors)
  }
}

