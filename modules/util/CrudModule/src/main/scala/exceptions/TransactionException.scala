/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package crud.exceptions

/**
  * Excepción de transacción
  */
case class TransactionException(message: String = "", cause: Throwable = None.orNull) extends Exception(message, cause)