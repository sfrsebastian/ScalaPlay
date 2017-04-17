package auth.logic

import java.util.UUID
import com.google.inject.Inject
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import auth.models._
import auth.persistence.{TokenPersistenceTrait, UserPersistenceTrait}
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/15/17.
  */
class AuthLogic @Inject() (userPersistence: UserPersistenceTrait, tokenPersistence: TokenPersistenceTrait) extends IdentityService[User] {

  def retrieve(loginInfo:LoginInfo) = userPersistence.get(loginInfo)

  def create(user:User) : Future[User] = userPersistence.create(user)

  def create(token:Token) : Future[Token] = tokenPersistence.create(token)

  def getUser(id:Int) : Future[Option[User]] = userPersistence.get(id)

  def getToken(uuid:UUID) : Future[Option[Token]] = tokenPersistence.get(uuid)

  def deleteToken(id:Int) : Future[Option[Token]] = tokenPersistence.delete(id)

  def confirm(loginInfo:LoginInfo) : Future[Option[User]] = userPersistence.confirm(loginInfo)
}