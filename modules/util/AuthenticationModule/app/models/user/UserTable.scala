package auth.models.user

/**
  * Created by sfrsebastian on 5/5/17.
  */
import java.util.UUID
import crud.models.Entity
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

class UserTable(tag:Tag) extends Entity[UserPersistenceModel](tag, "USERS") {
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
}
