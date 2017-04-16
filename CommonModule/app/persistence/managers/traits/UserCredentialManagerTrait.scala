package common.auth.persistence.traits

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import common.auth.models.{Profile, User}

import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/14/17.
  */
trait UserCredentialManagerTrait {
    def save(user:User):Future[User]
    def find(loginInfo:LoginInfo):Future[Option[User]]
    def find(userId:UUID):Future[Option[User]]
    def confirm(loginInfo:LoginInfo):Future[User]
    def link(profile:Profile):Future[User]
    def update(profile:Profile):Future[User]
}
