package auth.controllers

import com.mohiva.play.silhouette.api.actions.{SecuredRequest, UserAwareRequest}
import com.mohiva.play.silhouette.api.Silhouette
import auth.settings.AuthenticationEnvironment
import auth.models.user.User

/**
  * Created by sfrsebastian on 4/15/17.
  */
trait AuthenticationManager{
  def silhouette: Silhouette[AuthenticationEnvironment]
  def SecuredAction = silhouette.SecuredAction
  def UnsecuredAction = silhouette.UnsecuredAction
  def UserAwareAction = silhouette.UserAwareAction

  implicit def securedRequest2User[A](implicit request: SecuredRequest[AuthenticationEnvironment, A]): User = request.identity
  implicit def userAwareRequest2UserOpt[A](implicit request: UserAwareRequest[AuthenticationEnvironment, A]): Option[User] = request.identity
}
