package common.auth.logic

import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.impl.providers.CommonSocialProfile
import common.auth.models.{Profile, User}
import common.auth.persistence.traits.UserCredentialManagerTrait

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/15/17.
  */
class UserLogic @Inject() (cs:UserCredentialManagerTrait) extends IdentityService[User] {
  def retrieve(loginInfo:LoginInfo) = cs.find(loginInfo)

  def save(user:User) = cs.save(user)

  def find(id:UUID) = cs.find(id)

  def confirm(loginInfo:LoginInfo) = cs.confirm(loginInfo)

  def link(user:User, socialProfile:CommonSocialProfile) = {
    val profile = toProfile(socialProfile, user.id)
    if (user.profiles.exists(_.loginInfo == profile.loginInfo))
      Future.successful(user) else cs.link(profile)
  }

  def save(socialProfile:CommonSocialProfile, userId:Int) = {
    val profile = toProfile(socialProfile, userId)
    cs.find(profile.loginInfo).flatMap {
      case None => cs.save(User(1, UUID.randomUUID(), List(profile)))
      case Some(user) => cs.update(profile)
    }
  }

  private def toProfile(p:CommonSocialProfile, userId:Int) = Profile(
    id = 1,
    name = p.firstName.getOrElse(""),
    lastName = p.lastName,
    confirmed = true,
    email = p.email,
    fullName = p.fullName,
    avatarUrl = p.avatarURL,
    userId = userId,
    loginInfo = p.loginInfo,
    passwordInfo = None,
    oauth1Info = None
  )
}