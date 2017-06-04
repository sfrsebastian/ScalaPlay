/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package auth.persistence

import auth.models.user.{User, UserTable}
import slick.lifted.Rep
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Persistencia de usuario
  * Se inteyecta como delegado a Silhouette
  */
class UserPersistence extends UserPersistenceTrait {

  def updatePasswordInfo(loginInfo:LoginInfo, passwordInfo:PasswordInfo):DBIO[Option[User]] = {
    val projection: UserTable => (Rep[String], Rep[String], Rep[Option[String]]) = user => (user.hasher, user.password, user.salt)
    def transform(element:PasswordInfo) = (element.hasher, element.password, element.salt)
    for {
      result <- table.filter(u => u.loginProviderId === loginInfo.providerID && u.loginProviderKey === u.loginProviderKey).map(projection).update(transform(passwordInfo))
      updated <- getAction(table.filter(u => u.loginProviderId === loginInfo.providerID && u.loginProviderKey === u.loginProviderKey))
    } yield {
      result match{
        case 1 => updated
        case _ => None
      }
    }
  }

  def confirm(loginInfo:LoginInfo):DBIO[Option[User]] = {
    val projection: UserTable => Rep[Boolean] = user => user.confirmed
    for {
      result <- table.filter(u => u.loginProviderId === loginInfo.providerID && u.loginProviderKey === u.loginProviderKey).map(projection).update(true)
      updated <- getAction(table.filter(u => u.loginProviderId === loginInfo.providerID && u.loginProviderKey === u.loginProviderKey))
    } yield {
      result match{
        case 1 => updated
        case _ => None
      }
    }
  }
}
