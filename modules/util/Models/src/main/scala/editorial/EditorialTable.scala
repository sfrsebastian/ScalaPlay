package editorial.model

import crud.models.Entity
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

/**
  * Created by sfrsebastian on 5/6/17.
  */
class EditorialTable(tag:Tag) extends Entity[EditorialPersistenceModel](tag, "EDITORIALS") {
  def address = column[String]("ADDRESS")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id, name, address) <> (EditorialPersistenceModel.tupled, EditorialPersistenceModel.unapply _)
}
