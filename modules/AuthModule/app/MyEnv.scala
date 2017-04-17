package auth.settings
import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import auth.models.User

/**
  * Created by sfrsebastian on 4/15/17.
  */

trait MyEnv extends Env {
  type I = User
  type A = CookieAuthenticator
}

