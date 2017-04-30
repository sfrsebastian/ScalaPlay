package author.models

import crud.models.{Entity, Row}
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

/**
  * Created by sfrsebastian on 4/26/17.
  */
case class Author(id:Int, name:String, lastName:String) extends Row

class Authors(tag:Tag) extends Entity[Author](tag, "Authors") {
  def lastName = column[String]("LASTNAME")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id, name, lastName) <> (Author.tupled, Author.unapply _)
}
