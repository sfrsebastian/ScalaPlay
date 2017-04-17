package crud.models

import slick.jdbc.PostgresProfile.api._

abstract class Entity[T](tag:Tag, tableName:String) extends Table[T](tag, tableName){
  def id: Rep[Int] = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def name: Rep[String] = column[String]("NAME")
}
