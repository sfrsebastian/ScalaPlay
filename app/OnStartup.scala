package settings

import com.google.inject.Inject
import auth.models.{Token, Tokens, User, Users}
import crud.DatabaseOperations
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._
import book.models.{Book, Books}
import play.api.Configuration
import uk.co.jemos.podam.api.PodamFactoryImpl

class OnStartup @Inject()(configuration:Configuration) {
  val factory = new PodamFactoryImpl
  val db = Database.forConfig("Database")
  val books = TableQuery[Books]
  val users = TableQuery[Users]
  val tokens = TableQuery[Tokens]

  if(configuration.getBoolean("dropCreate").getOrElse(false)){
    DatabaseOperations.DropCreate[Book, Books](db, books)
    DatabaseOperations.DropCreate[User, Users](db, users)
    DatabaseOperations.DropCreate[Token, Tokens](db, tokens)
  }
  DatabaseOperations.createIfNotExist[Book, Books](db, books)
  DatabaseOperations.createIfNotExist[Token, Tokens](db, tokens)
  DatabaseOperations.createIfNotExist[User, Users](db, users)
  if(configuration.getBoolean("seed").getOrElse(false)){
    val seedCollection = for {
      _ <- 0 to 19
    }yield factory.manufacturePojo(classOf[Book])
    db.run(books ++= seedCollection)
  }
}

