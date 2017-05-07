package crud

import slick.jdbc.PostgresProfile.api._
import slick.jdbc.meta.MTable
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Created by sfrsebastian on 4/11/17.
  */
object DatabaseOperations{

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
