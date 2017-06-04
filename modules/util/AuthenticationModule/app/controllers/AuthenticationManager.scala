/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package auth.controllers

import com.mohiva.play.silhouette.api.actions.{SecuredRequest, UserAwareRequest}
import com.mohiva.play.silhouette.api.Silhouette
import auth.settings.AuthenticationEnvironment
import auth.models.user.User

/**
  * Define las acciones de autenticación y autorización
  */
trait AuthenticationManager{
  def silhouette: Silhouette[AuthenticationEnvironment]
  def SecuredAction = silhouette.SecuredAction
  def UnsecuredAction = silhouette.UnsecuredAction
  def UserAwareAction = silhouette.UserAwareAction

  implicit def securedRequest2User[A](implicit request: SecuredRequest[AuthenticationEnvironment, A]): User = request.identity
  implicit def userAwareRequest2UserOpt[A](implicit request: UserAwareRequest[AuthenticationEnvironment, A]): Option[User] = request.identity
}
