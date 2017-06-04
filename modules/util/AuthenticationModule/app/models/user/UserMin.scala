/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package auth.models.user

/**
  * Modelo de usuario minificado
  * @param id
  * @param name
  * @param lastName
  * @param fullName
  * @param email
  */
case class UserMin(id:Int, name:String, lastName:String, fullName:String, email:String)