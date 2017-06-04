/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package auth.settings

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import auth.models.user.User

/**
  * El ambiente de autenticación de Silhouette
  */
trait AuthenticationEnvironment extends Env {
  type I = User
  type A = JWTAuthenticator
}

