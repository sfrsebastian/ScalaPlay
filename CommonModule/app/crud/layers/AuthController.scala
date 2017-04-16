package common.traits.layers

import com.mohiva.play.silhouette.api.actions.{SecuredRequest, UserAwareRequest}
import com.mohiva.play.silhouette.api.{Environment, Silhouette}
import common.auth.models.User
import common.settings.auth.MyEnv
import play.api.i18n.I18nSupport
import play.api.mvc.Controller
import play.mvc.Security

/**
  * Created by sfrsebastian on 4/15/17.
  */
trait AuthController extends I18nSupport {
  def silhouette: Silhouette[MyEnv]
  def env: Environment[MyEnv] = silhouette.env

  def SecuredAction = silhouette.SecuredAction
  def UnsecuredAction = silhouette.UnsecuredAction
  def UserAwareAction = silhouette.UserAwareAction

  implicit def securedRequest2User[A](implicit request: SecuredRequest[MyEnv, A]): User = request.identity
  implicit def userAwareRequest2UserOpt[A](implicit request: UserAwareRequest[MyEnv, A]): Option[User] = request.identity
}
