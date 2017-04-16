package settings

import com.google.inject.Inject
import common.auth.models.Profile
import common.auth.persistence.entities.{Profiles, UserModel, Users}
import common.utilities.DatabaseOperations
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._
import models.bookModule.{Book, Books}
import play.api.Configuration

class OnStartup @Inject()(configuration:Configuration) {
  val db = Database.forConfig("Database")
  val books = TableQuery[Books]
  val users = TableQuery[Users]
  val profiles = TableQuery[Profiles]

  if(configuration.getBoolean("dropCreate").getOrElse(false)){
    DatabaseOperations.DropCreate[Book, Books](db, books)
    DatabaseOperations.DropCreate[Profile, Profiles](db, profiles)
    DatabaseOperations.DropCreate[UserModel, Users](db, users)
  }
  DatabaseOperations.createIfNotExist[Book, Books](db, books)
  DatabaseOperations.createIfNotExist[Profile, Profiles](db, profiles)
  DatabaseOperations.createIfNotExist[UserModel, Users](db, users)

}

