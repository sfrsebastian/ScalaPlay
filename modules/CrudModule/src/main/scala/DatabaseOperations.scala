package crud

import slick.jdbc.PostgresProfile.api._
import slick.jdbc.meta.MTable
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * Created by sfrsebastian on 4/11/17.
  */
object DatabaseOperations{

  def createIfNotExist[T, K<:Table[T]](database:Database, table:TableQuery[K]) = {
    val existing = database.run(MTable.getTables)
    def f = existing.flatMap( v => {
      val names = v.map(mt => mt.name.name)
      if(!names.contains(table.baseTableRow.tableName)){
        println("Create de tabla " + table.baseTableRow.tableName)
        database.run(table.schema.create)
      }
      existing
    })
    Await.result(f, Duration.Inf)
  }

  def DropCreate[T, K<:Table[T]](database:Database, table:TableQuery[K]) = {
    val existing = database.run(MTable.getTables)
    def f = existing.flatMap( v => {
      val names = v.map(mt => mt.name.name)
      if(names.contains(table.baseTableRow.tableName)){
        println("Drop Create de tabla " + table.baseTableRow.tableName)
        database.run(table.schema.drop.andThen(table.schema.create))
      }
      existing
    })
    Await.result(f, Duration.Inf)
  }

  def Drop[T ,K <: Table[T]](database:Database, table:TableQuery[K]) = {
    val existing = database.run(MTable.getTables)
    def f = existing.flatMap( v => {
      val names = v.map(mt => mt.name.name)
      if(!names.contains(table.baseTableRow.tableName)){
        println("Drop de tabla " + table.baseTableRow.tableName)
        database.run(table.schema.drop)
      }
      existing
    })
    Await.result(f, Duration.Inf)
  }
}
