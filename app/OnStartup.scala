package settings

import com.google.inject.Inject
import auth.models.user.UserTable
import crud.DatabaseOperations
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._
import persistence.DatabasePopulator
import play.api.Configuration
import uk.co.jemos.podam.api.PodamFactoryImpl
import scala.concurrent.duration._
import scala.concurrent.Await

class OnStartup @Inject()(configuration:Configuration) {
  val factory = new PodamFactoryImpl

  implicit  val db = Database.forConfig("Database")

  val tables = DatabasePopulator.tables :+ TableQuery[UserTable]

  if(configuration.getBoolean("dropCreate").getOrElse(false)){
    val dropSequence = db.run(DBIO.sequence(tables.reverse.flatMap(table => DatabaseOperations.Drop(db, table))))
    Await.result(dropSequence, 10.second)
  }

  val createSequence = db.run(DBIO.sequence(tables.flatMap(table => DatabaseOperations.createIfNotExist(db, table))))
  Await.result(createSequence, 10.second)

  if(configuration.getBoolean("seed").getOrElse(false)){
    db.run(DatabasePopulator.populate)
  }
}