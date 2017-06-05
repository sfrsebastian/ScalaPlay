/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package editorial.model

import crud.models.Entity
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

class EditorialTable(tag:Tag) extends Entity[EditorialPersistenceModel](tag, "EDITORIALS") {
  def address = column[String]("ADDRESS")
  def * = (id, name, address) <> (EditorialPersistenceModel.tupled, EditorialPersistenceModel.unapply _)
}
