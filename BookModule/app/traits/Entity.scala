package traits

import slick.jdbc.PostgresProfile.api._

/**
  * Created by sfrsebastian on 3/14/17.
  */
abstract class Entity[T](tag:Tag, tableName:String) extends Table[T](tag, tableName){
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME")
}
