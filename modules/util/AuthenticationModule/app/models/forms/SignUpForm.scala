/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package auth.models.forms

/**
  * Modelo de formulario de creación de usuario
  * @param name
  * @param lastName
  * @param email
  * @param password
  */
case class SignUpForm(name:String,lastName:String, email:String, password:String)
