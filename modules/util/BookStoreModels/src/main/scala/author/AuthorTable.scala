package models.author

import crud.models.Entity
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

/**
  * Created by sfrsebastian on 4/30/17.
  */
class AuthorTable(tag:Tag) extends Entity[AuthorPersistenceModel](tag, "AUTHORS") {
  def lastName = column[String]("LAST_NAME")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id, name, lastName) <> (AuthorPersistenceModel.tupled, AuthorPersistenceModel.unapply _)
}
