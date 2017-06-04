/*
 * Realizado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package auth.models.rule

/**
  * Modelo de una regla de configuración
  * @param authentication El tipo de autenticación
  * @param authorization El tipo de autorización
  * @param path La ruta a agregar seguridad
  * @param authorizationOp El operador de autorización
  * @param method El método de la ruta
  */
case class Rule(val authentication:String, authorization:Option[List[String]], path:String, authorizationOp:Option[String], method:String)