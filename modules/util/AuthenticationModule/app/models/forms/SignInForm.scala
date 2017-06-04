/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package auth.models.forms

/**
  * Modelo de formulario de inicio de sesión
  * @param email El email del usuario
  * @param password La contraseña del usuario
  * @param rememberMe Indica si se debe recordar al usuario
  */
case class SignInForm(email:String, password:String, rememberMe:Boolean)
