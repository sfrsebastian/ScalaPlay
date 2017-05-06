package auth.logic

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import auth.models.user.User
import auth.persistence.UserPersistenceTrait
import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

/**
  * Created by sfrsebastian on 4/15/17.
  */
class AuthLogic @Inject() (userPersistence: UserPersistenceTrait) extends IdentityService[User] {

  def retrieve(loginInfo:LoginInfo) = userPersistence.runAction(userPersistence.getAction(userPersistence.table.filter(u => u.loginProviderId === loginInfo.providerID && u.loginProviderKey === u.loginProviderKey)))

  def create(user:User) : Future[User] = userPersistence.runAction(userPersistence.createAction(user))

  def getUser(id:Int) : Future[Option[User]] = userPersistence.runAction(userPersistence.getAction(userPersistence.table.filter(_.id === id)))

  def confirm(loginInfo:LoginInfo) : Future[Option[User]] = userPersistence.runAction(userPersistence.confirm(loginInfo))
}