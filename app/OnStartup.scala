/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package settings

import com.google.inject.Inject
import auth.models.user.UserTable
import crud.DatabaseOperations
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._
import persistence.DatabasePopulator
import play.api.Configuration
import scala.concurrent.duration._
import scala.concurrent.Await

/**
  * @param configuration La configuración de la aplicación
  */
class OnStartup @Inject()(configuration:Configuration) {

  //La base de datos de la aplicación
  implicit val db = Database.forConfig("Database")

  //Las tablas de la aplicación
  val tables = DatabasePopulator.tables :+ TableQuery[UserTable]

  //Si se encuentra la configuración dropCreate, se eliminan las tablas y se vuelven a crear
  if(configuration.getBoolean("dropCreate").getOrElse(false)){
    val dropSequence = db.run(DBIO.sequence(tables.reverse.flatMap(table => DatabaseOperations.Drop(db, table))))
    Await.result(dropSequence, 10.second)
  }

  //Se crean las tablas si no existen
  val createSequence = db.run(DBIO.sequence(tables.flatMap(table => DatabaseOperations.createIfNotExist(db, table))))
  Await.result(createSequence, 10.second)

  //Si se encuentra la confiruación seed, se llena la base de datos.
  if(configuration.getBoolean("seed").getOrElse(false)){
    db.run(DatabasePopulator.populate)
  }
}