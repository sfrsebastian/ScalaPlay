package common.auth.persistence.managers

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.OAuth1Info
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sfrsebastian on 4/15/17.
  */
class OAuth1Manager extends DelegableAuthInfoDAO[OAuth1Info]{
    def find(loginInfo:LoginInfo):Future[Option[OAuth1Info]] = {
      Future(None)
    }

    def add(loginInfo:LoginInfo, authInfo:OAuth1Info):Future[OAuth1Info] = {
      Future(OAuth1Info("",""))
    }

    def update(loginInfo:LoginInfo, authInfo:OAuth1Info):Future[OAuth1Info] = add(loginInfo, authInfo)

    def save(loginInfo:LoginInfo, authInfo:OAuth1Info):Future[OAuth1Info] = add(loginInfo, authInfo)

    def remove(loginInfo:LoginInfo):Future[Unit] = {
      Future()
    }
}
