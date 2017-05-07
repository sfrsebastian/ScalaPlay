package auth.models.role

import auth.models.user.User
import auth.settings.AuthenticationEnvironment
import com.mohiva.play.silhouette.api.Authorization
import play.api.mvc.Request
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/17/17.
  */

/**
  * Only allows those users that have at least a service of the selected.
  * Master service is always allowed.
  * Ex: WithService("serviceA", "serviceB") => only users with services "serviceA" OR "serviceB" (or "master") are allowed.
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
  * Only allows those users that have every of the selected services.
  * Master service is always allowed.
  * Ex: Restrict("serviceA", "serviceB") => only users with services "serviceA" AND "serviceB" (or "master") are allowed.
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


