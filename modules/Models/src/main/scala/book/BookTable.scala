/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package book.model

import crud.models.Entity
import editorial.model.EditorialTable
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

class BookTable(tag:Tag) extends Entity[BookPersistenceModel](tag, "BOOKS") {
  def description = column[String]("DESCRIPTION")
  def ISBN = column[String]("ISBN")
  def image = column[String]("IMAGE")
  def editorialId = column[Option[Int]]("EDITORIAL_ID")

  def editorialFK = foreignKey("EDITORIAL_FK", editorialId, TableQuery[EditorialTable])(_.id, onUpdate=ForeignKeyAction.Restrict)
  def * = (id, name, description, ISBN, image, editorialId) <> (BookPersistenceModel.tupled, BookPersistenceModel.unapply _)
}
