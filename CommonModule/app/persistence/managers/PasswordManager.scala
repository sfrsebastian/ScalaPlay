package common.auth.persistence.managers

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/15/17.
  */
class PasswordManager extends DelegableAuthInfoDAO[PasswordInfo]{

  def find(loginInfo:LoginInfo):Future[Option[PasswordInfo]] = {
    Future(None)
  }

  def add(loginInfo:LoginInfo, authInfo:PasswordInfo):Future[PasswordInfo] = {
    Future(PasswordInfo("","",None))
  }

  def update(loginInfo:LoginInfo, authInfo:PasswordInfo):Future[PasswordInfo] = add(loginInfo, authInfo)

  def save(loginInfo:LoginInfo, authInfo:PasswordInfo):Future[PasswordInfo] = add(loginInfo, authInfo)

  def remove(loginInfo:LoginInfo):Future[Unit] = {
    Future()
  }
}
