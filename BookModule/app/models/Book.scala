package models.bookModule
import common.traits.model.{Entity, Row}
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

case class Book(id:Int, name:String, description:String, ISBN:String, image:String) extends Row

class Books(tag:Tag) extends Entity[Book](tag, "BOOKS") {
  def description = column[String]("DESCRIPTION")
  def ISBN = column[String]("ISBN")
  def image = column[String]("IMAGE")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id, name, description, ISBN, image) <> (Book.tupled, Book.unapply _)
}