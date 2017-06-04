/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package crud.models

/**
  * Clase abstracta que define un modelo basico
  */
abstract class Row {
  /**
    * El id de la entidad
    */
  val id : Int

  /**
    * El nombre de la entidad
    */
  val name : String
}