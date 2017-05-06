package auth.models.user

/**
  * Created by sfrsebastian on 5/5/17.
  */
import java.util.UUID
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import crud.models.Entity
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

class UserTable(tag:Tag) extends Entity[UserPersistenceModel](tag, "USERS") {
  //private type LoginInfoTuple = (String, String)
  //private type PasswordInfoTuple = (String, String, Option[String])
  //private type UserTuple = (Int, UUID, String, String, Boolean, String, LoginInfoTuple, PasswordInfoTuple, String)

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
  def roles = column[String]("ROLES")

  def * = (id, uuid, name, lastName, confirmed, email, loginProviderId, loginProviderKey, hasher, password, salt, roles) <> (UserPersistenceModel.tupled, UserPersistenceModel.unapply _)

  /*def * = userShapedValue.shaped <> (toModel, toTuple)

  private val userShapedValue = (
    id, uuid, name, lastName, confirmed, email,
    (loginProviderId, loginProviderKey),
    (hasher, password, salt), roles
  )

  private val toModel: UserTuple => User = {user=>
    User(
      user._1, user._2, user._3, user._4, user._5, user._6,
      loginInfo = LoginInfo.tupled.apply(user._7),
      passwordInfo = PasswordInfo.tupled.apply(user._8),
      roles = user._9.split(",")
    )
  }

  private val toTuple: User => Option[UserTuple] = { user =>
    Some(
      user.id, user.uuid, user.name, user.lastName, user.confirmed, user.email,
      (LoginInfo.unapply(user.loginInfo).get),
      (PasswordInfo.unapply(user.passwordInfo).get),
      user.roles.mkString(",")
    )
  }*/
}
