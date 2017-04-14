package common.traits.model

import slick.jdbc.PostgresProfile.api._

abstract class Entity[T](tag:Tag, tableName:String) extends Table[T](tag, tableName){
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME")
}
