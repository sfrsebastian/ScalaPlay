package common

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.meta.MTable
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sfrsebastian on 4/11/17.
  */
object DatabaseOperations{

  def createIfNotExist[T, K<:Table[T]](database:Database, tables:Seq[TableQuery[K]]) = {
    val existing = database.run(MTable.getTables)
    def f = existing.flatMap( v => {
      val names = v.map(mt => mt.name.name)
      val createIfNotExist = tables.filter( table =>
        (!names.contains(table.baseTableRow.tableName)))
        .map(table => {
          println("Create de tabla " + table.baseTableRow.tableName)
          table.schema.create
        })
      database.run(DBIO.sequence(createIfNotExist))
    })
    Await.result(f, Duration.Inf)
  }

  def DropCreate[T, K<:Table[T]](database:Database, tables:Seq[TableQuery[K]]) = {
    val existing = database.run(MTable.getTables)
    def f = existing.flatMap( v => {
      val names = v.map(mt => mt.name.name)
      val dropCreate = tables.filter( table =>
        (names.contains(table.baseTableRow.tableName)))
        .map(table => {
          println("Drop de tabla " + table.baseTableRow.tableName)
          table.schema.drop.andThen(table.schema.create)
        })
      database.run(DBIO.sequence(dropCreate))
    })
    Await.result(f, Duration.Inf)
  }
}
