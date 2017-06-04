/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */

package crud

import slick.jdbc.PostgresProfile.api._
import slick.jdbc.meta.MTable
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Define las operaciones a realizar en la base de datos
  */
object DatabaseOperations{

  /**
    * Crea la tabla dada en caso de que no exista
    * @param database La base de datos donde ejecutar la acción
    * @param table La tabla donde ejecutar la acción
    * @tparam K
    * @return
    */
  def createIfNotExist[K<:Table[_]](database:Database, table:TableQuery[K]) = {
    val existing = database.run(MTable.getTables)
    val f = existing.map( v => {
      val names = v.map(mt => mt.name.name)
      if(!names.contains(table.baseTableRow.tableName)){
        println("CREATE de tabla " + table.baseTableRow.tableName)
        Some(table.schema.create)
      }
      else{
        None
      }
    })
    Await.result(f, 10.second)
  }

  /**
    * Elimina la tabla dada
    * @param database La base de datos donde ejecutar la acción
    * @param table La tabla donde ejecutar la acción
    * @tparam K
    * @return
    */
  def Drop[K <: Table[_]](database:Database, table:TableQuery[K]) = {
    val existing = database.run(MTable.getTables)
    def f = existing.map( v => {
      val names = v.map(mt => mt.name.name)
      if(names.contains(table.baseTableRow.tableName)){
        println("DROP de tabla " + table.baseTableRow.tableName)
        Some(table.schema.drop)
      }
      else{
        None
      }
    })
    Await.result(f, 10.second)
  }
}
