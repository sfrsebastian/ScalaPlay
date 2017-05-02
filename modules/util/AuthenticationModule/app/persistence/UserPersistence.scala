package auth.persistence

import java.util.UUID
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import auth.models.{User, Users}
import crud.layers.CrudPersistence
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Rep
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/14/17.
  */

trait UserPersistenceTrait extends CrudPersistence[User, Users] {

  override var table: TableQuery[Users] = Users.table

  val updateProjection: Users => (Rep[Boolean], Rep[String], Rep[String]) = b => (b.confirmed, b.name, b.lastName)

  def updateTransform(element:User): (Boolean, String, String) = (element.confirmed, element.name, element.lastName)

  def updatePasswordInfo(loginInfo:LoginInfo, passwordInfo:PasswordInfo):Future[Option[User]] = {
    val projection: Users => (Rep[String], Rep[String], Rep[Option[String]]) = user => (user.hasher, user.password, user.salt)
    def transform(element:PasswordInfo) = (element.hasher, element.password, element.salt)
    for {
      result <- db.run(table.filter(u => u.loginProviderId === loginInfo.providerID && u.loginProviderKey === u.loginProviderKey).map(projection).update(transform(passwordInfo)))
      updated <- db.run(getAction(table.filter(u => u.loginProviderId === loginInfo.providerID && u.loginProviderKey === u.loginProviderKey)))
    } yield {
      result match{
        case 1 => updated
        case _ => None
      }
    }
  }

  def confirm(loginInfo:LoginInfo):Future[Option[User]] = {
    val projection: Users => Rep[Boolean] = user => user.confirmed
    for {
      result <- db.run(table.filter(u => u.loginProviderId === loginInfo.providerID && u.loginProviderKey === u.loginProviderKey).map(projection).update(true))
      updated <- db.run(getAction(table.filter(u => u.loginProviderId === loginInfo.providerID && u.loginProviderKey === u.loginProviderKey)))
    } yield {
      result match{
        case 1 => updated
        case _ => None
      }
    }
  }

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
}

class UserPersistence extends UserPersistenceTrait
