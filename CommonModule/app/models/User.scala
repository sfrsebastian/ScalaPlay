package common.auth.models

import java.util.UUID
import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import com.mohiva.play.silhouette.api.util.PasswordInfo
import common.traits.model.{Entity, Row}
import play.api.libs.json.Json
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

/**
  * Created by sfrsebastian on 4/14/17.
  */
case class User(
  id:Int,
  uuid:UUID,
  name: String,
  lastName: String,
  confirmed: Boolean,
  email:String,
  loginInfo:LoginInfo,
  passwordInfo:PasswordInfo
) extends Row with Identity{
  def fullName = name + " " + lastName
  def toMin = UserMin(name, lastName, email)
}

case class UserMin(name:String, lastName:String, email:String)

object UserMin{
  implicit val userMinJsonFormat = Json.format[UserMin]
}

object User{
  implicit val passwordInfoJsonFormat = Json.format[PasswordInfo]
  implicit val userJsonFormat = Json.format[User]
  def generateUser(form: SignUpForm, loginInfo: LoginInfo, passwordInfo: PasswordInfo) = User(1, UUID.randomUUID(), form.name, form.lastName, false, form.email, loginInfo, passwordInfo)
}

object Users {
  val table = TableQuery[Users]
}

class Users(tag:Tag) extends Entity[User](tag, "USERS") {
  private type LoginInfoTuple = (String, String)
  private type PasswordInfoTuple = (String, String, Option[String])
  private type UserTuple = (Int, UUID, String, String, Boolean, String, LoginInfoTuple, PasswordInfoTuple)

  def uuid = column[UUID]("UUID")
  override def name = column[String]("FIRST_NAME")
  def lastName = column[String]("LAST_NAME")
  def confirmed = column[Boolean]("CONFIRMED")
  def email = column[String]("EMAIL")
  def loginProviderId = column[String]("PROVIDER_ID") //Login info
  def loginProviderKey = column[String]("PROVIDER_KEY") //Login info
  def hasher = column[String]("HASHER") //PasswordInfo
  def password = column[String]("PASSWORD") //PasswordInfo
  def salt = column[Option[String]]("SALT") //PasswordInfo

  def * = userShapedValue.shaped <> (toModel, toTuple)

  private val userShapedValue = (
    id, uuid, name, lastName, confirmed, email,
    (loginProviderId, loginProviderKey),
    (hasher, password, salt)
  )

  private val toModel: UserTuple => User = {user=>
    User(
      user._1, user._2, user._3, user._4, user._5, user._6,
      loginInfo = LoginInfo.tupled.apply(user._7),
      passwordInfo = PasswordInfo.tupled.apply(user._8)
    )
  }

  private val toTuple: User => Option[UserTuple] = { user =>
    Some(
      user.id, user.uuid, user.name, user.lastName, user.confirmed, user.email,
      (LoginInfo.unapply(user.loginInfo).get),
      (PasswordInfo.unapply(user.passwordInfo).get)
    )
  }
}
