/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package crud.models

import slick.jdbc.PostgresProfile.api._

/**
  * Clase abstracta que representa una tabla
  * @param tableName El nombre de la tabla
  * @tparam T El Modelo de persistencia correspondiente
  */
abstract class Entity[T](tag:Tag, tableName:String) extends Table[T](tag, tableName){
  /**
    * Columna con el id de las entidades
    */
  val id: Rep[Int] = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  /**
    * Columna con el nombre de las entidades
    */
  val name: Rep[String] = column[String]("NAME")
}