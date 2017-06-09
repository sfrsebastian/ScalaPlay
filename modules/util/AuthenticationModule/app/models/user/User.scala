/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package auth.models.user

import java.util.UUID
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import crud.models.Row
import auth.models.forms.SignUpForm
import play.api.libs.json.Json

/**
  * Modelo de usuario
  * @param id
  * @param uuid
  * @param name
  * @param lastName
  * @param confirmed
  * @param email
  * @param loginInfo
  * @param passwordInfo
  * @param roles
  */
case class User(
  id:Int,
  uuid:UUID,
  name: String,
  lastName: String,
  confirmed: Boolean,
  email:String,
  loginInfo:LoginInfo,
  passwordInfo:PasswordInfo,
  roles:Array[String] = Array("user")
) extends Row with Identity{
  def fullName = name + " " + lastName
  def toMin = UserMin(id, name, lastName, fullName, email)
}

/**
  * Objeto de compañia de modelo de usuario
  */
object User{
  implicit val passwordInfoJsonFormat = Json.format[PasswordInfo]
  implicit val userJsonFormat = Json.format[User]
  def generateUser(form: SignUpForm, loginInfo: LoginInfo, passwordInfo: PasswordInfo) = User(1, UUID.randomUUID(), form.name, form.lastName, false, form.email, loginInfo, passwordInfo)
}
