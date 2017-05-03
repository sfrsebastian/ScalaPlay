package book.model

import crud.models.Entity
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

/**
  * Created by sfrsebastian on 4/30/17.
  */
class BookTable(tag:Tag) extends Entity[BookPersistenceModel](tag, "BOOKS") {
  def description = column[String]("DESCRIPTION")
  def ISBN = column[String]("ISBN")
  def image = column[String]("IMAGE")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id, name, description, ISBN, image) <> (BookPersistenceModel.tupled, BookPersistenceModel.unapply _)
}
