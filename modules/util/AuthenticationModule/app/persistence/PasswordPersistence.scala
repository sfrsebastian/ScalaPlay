package auth.persistence

import javax.inject.Inject
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

/**
  * Created by sfrsebastian on 4/15/17.
  */
class PasswordPersistence @Inject()(userPersistence:UserPersistenceTrait) extends DelegableAuthInfoDAO[PasswordInfo]{

  def find(loginInfo:LoginInfo):Future[Option[PasswordInfo]] = {
    userPersistence.runAction(userPersistence.getAction(userPersistence.table.filter(u => u.loginProviderId === loginInfo.providerID && u.loginProviderKey === u.loginProviderKey))).map{
      case Some(user) => Some(user.passwordInfo)
      case None => None
    }
  }

  def add(loginInfo:LoginInfo, passwordInfo:PasswordInfo):Future[PasswordInfo] = {
    userPersistence.updatePasswordInfo(loginInfo, passwordInfo).map{
      case Some(user) => user.passwordInfo
      case None => PasswordInfo("","",None)
    }
  }

  def update(loginInfo:LoginInfo, authInfo:PasswordInfo):Future[PasswordInfo] = {
    add(loginInfo, authInfo)
  }

  def save(loginInfo:LoginInfo, passwordInfo:PasswordInfo):Future[PasswordInfo] = {
    add(loginInfo, passwordInfo)
  }

  def remove(loginInfo:LoginInfo):Future[Unit] = {
    Future.successful(add(loginInfo, PasswordInfo("","",None)))
  }
}
