package common.auth.models

import java.util.UUID

import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import com.mohiva.play.silhouette.impl.providers.OAuth1Info
import play.api.libs.json.Json

case class User(id:Int, uuid: UUID, profiles: List[Profile]) extends Identity {
  def profileFor(loginInfo:LoginInfo) = profiles.find(_.loginInfo == loginInfo)
  def fullName(loginInfo:LoginInfo) = profileFor(loginInfo).flatMap(_.fullName)
}

object User {
  implicit val passwordInfoJsonFormat = Json.format[PasswordInfo]
  implicit val oauth1InfoJsonFormat = Json.format[OAuth1Info]
  implicit val profileJsonFormat = Json.format[Profile]
  implicit val userJsonFormat = Json.format[User]
}



