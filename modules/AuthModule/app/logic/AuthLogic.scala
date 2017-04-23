package auth.logic

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import auth.models._
import auth.persistence.{UserPersistenceTrait}
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/15/17.
  */
class AuthLogic @Inject() (userPersistence: UserPersistenceTrait) extends IdentityService[User] {

  def retrieve(loginInfo:LoginInfo) = userPersistence.get(loginInfo)

  def create(user:User) : Future[User] = userPersistence.create(user)

  def getUser(id:Int) : Future[Option[User]] = userPersistence.get(id)

  def confirm(loginInfo:LoginInfo) : Future[Option[User]] = userPersistence.confirm(loginInfo)
}