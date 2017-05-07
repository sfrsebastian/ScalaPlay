package auth.models.user

import java.util.UUID
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import crud.models.Row
import auth.models.forms.SignUpForm
import play.api.libs.json.Json

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
  passwordInfo:PasswordInfo,
  roles:Array[String] = Array("user", "admin")
) extends Row with Identity{
  def fullName = name + " " + lastName
  def toMin = UserMin(id, name, lastName, fullName, email)
}

object User{
  implicit val passwordInfoJsonFormat = Json.format[PasswordInfo]
  implicit val userJsonFormat = Json.format[User]
  def generateUser(form: SignUpForm, loginInfo: LoginInfo, passwordInfo: PasswordInfo) = User(1, UUID.randomUUID(), form.name, form.lastName, false, form.email, loginInfo, passwordInfo)
}
