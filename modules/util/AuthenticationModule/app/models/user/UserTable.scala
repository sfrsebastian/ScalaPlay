/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package auth.models.user

import java.util.UUID
import crud.models.Entity
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

/**
  * Modelo de tabla de usuario
  * @param tag
  */
class UserTable(tag:Tag) extends Entity[UserPersistenceModel](tag, "USERS") {
  def uuid = column[UUID]("UUID")
  override val name = column[String]("FIRST_NAME")
  val lastName = column[String]("LAST_NAME")
  val confirmed = column[Boolean]("CONFIRMED")
  val email = column[String]("EMAIL")
  val loginProviderId = column[String]("PROVIDER_ID") //Login info
  val loginProviderKey = column[String]("PROVIDER_KEY") //Login info
  val hasher = column[String]("HASHER") //PasswordInfo
  val password = column[String]("PASSWORD") //PasswordInfo
  val salt = column[Option[String]]("SALT") //PasswordInfo
  val roles = column[String]("ROLES")

  def * = (id, uuid, name, lastName, confirmed, email, loginProviderId, loginProviderKey, hasher, password, salt, roles) <> (UserPersistenceModel.tupled, UserPersistenceModel.unapply _)
}
