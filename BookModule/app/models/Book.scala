package models.bookModule
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._
import common.Entity

case class Book(id:Int, name:String, description:String, ISBN:String, image:String)

class Books(tag:Tag) extends Entity[Book](tag, "BOOKS") {
  def description = column[String]("DESCRIPTION")
  def ISBN = column[String]("ISBN")
  def image = column[String]("IMAGE")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id, name, description, ISBN, image) <> (Book.tupled, Book.unapply _)
}