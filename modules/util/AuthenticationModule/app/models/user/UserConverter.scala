package auth.models.user

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import crud.models.ModelConverter
/**
  * Created by sfrsebastian on 4/30/17.
  */
object UserPersistenceConverter extends ModelConverter[User, UserPersistenceModel] {
  override def convert(source: User): UserPersistenceModel = {
    UserPersistenceModel(source.id, source.uuid, source.name, source.lastName, source.confirmed,source.email,source.loginInfo.providerID, source.loginInfo.providerKey, source.passwordInfo.salt.getOrElse(""), source.passwordInfo.password, Some(source.passwordInfo.hasher), source.roles.mkString(","))
  }

  override def convertInverse(source: UserPersistenceModel) = {
    val loginInfo = LoginInfo(source.loginProviderID, source.loginProviderKey)
    val passwordInfo = PasswordInfo(source.hasher, source.password, source.salt)
    User(source.id, source.uuid, source.name, source.lastName, source.confirmed,source.email, loginInfo, passwordInfo, source.roles.split(","))
  }
}