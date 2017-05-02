package auth.settings
import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.{CookieAuthenticator, JWTAuthenticator}
import auth.models.User

/**
  * Created by sfrsebastian on 4/15/17.
  */

trait AuthenticationEnvironment extends Env {
  type I = User
  type A = JWTAuthenticator
}

