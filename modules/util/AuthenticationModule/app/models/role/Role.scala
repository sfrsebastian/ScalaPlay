/*
 * Realizado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package auth.models.role

import auth.models.user.User
import auth.settings.AuthenticationEnvironment
import com.mohiva.play.silhouette.api.Authorization
import play.api.mvc.Request
import scala.concurrent.Future

/**
  * Autoriza a los usuarios que cuenten con alguno de los roles dados o rol de administrador
  */
case class WithRole(anyOf: String*) extends Authorization[User, AuthenticationEnvironment#A] {
  def isAuthorized[A](user: User, authenticator: AuthenticationEnvironment#A)(implicit r: Request[A]) = Future.successful {
    WithRole.isAuthorized(user, anyOf: _*)
  }
}
object WithRole {
  def isAuthorized(user: User, anyOf: String*): Boolean =
    anyOf.intersect(user.roles).size > 0 || user.roles.contains("admin")
}

/**
  * Autoriza a los usuarios que cuenten con todos los roles dados o rol de administrador
  */
case class WithRoles(allOf: String*) extends Authorization[User, AuthenticationEnvironment#A] {
  def isAuthorized[A](user: User, authenticator: AuthenticationEnvironment#A)(implicit r: Request[A]) = Future.successful {
    WithRoles.isAuthorized(user, allOf: _*)
  }
}
object WithRoles {
  def isAuthorized(user: User, allOf: String*): Boolean =
    allOf.intersect(user.roles).size == allOf.size || user.roles.contains("admin")
}


