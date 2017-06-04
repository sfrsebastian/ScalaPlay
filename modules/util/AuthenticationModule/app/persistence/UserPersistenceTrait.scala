/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package auth.persistence

import slick.jdbc.PostgresProfile.api._
import slick.lifted.Rep
import scala.concurrent.ExecutionContext.Implicits.global
import auth.models.user._
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import layers.persistence.CrudPersistence

/**
  * Persistencia Crud de usuario
  */
trait UserPersistenceTrait extends CrudPersistence[User, UserPersistenceModel, UserTable] {

  override var table = TableQuery[UserTable]

  override implicit val Model2Persistence = UserPersistenceConverter

  val updateProjection: UserTable => (Rep[Boolean], Rep[String], Rep[String]) = b => (b.confirmed, b.name, b.lastName)

  def updateTransform(element:UserPersistenceModel): (Boolean, String, String) = (element.confirmed, element.name, element.lastName)

  override def updateAction(id: Int, toUpdate: User): DBIO[Option[User]] = {
    for {
      result <- table.filter(_.id === id).map(updateProjection).update(updateTransform(toUpdate))
      updated <- getAction(table.filter(_.id === id))
    }yield{
      result match{
        case 1=>updated
        case _=>None
      }
    }
  }

  def confirm(loginInfo:LoginInfo):DBIO[Option[User]]

  def updatePasswordInfo(loginInfo:LoginInfo, passwordInfo:PasswordInfo):DBIO[Option[User]]
}
