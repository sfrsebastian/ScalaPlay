/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package crud.exceptions

/**
  * Excepción de lógica de negocio
  */
case class LogicLayerException (message: String = "", cause: Throwable = None.orNull) extends Exception(message, cause)