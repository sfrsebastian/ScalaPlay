/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package review.model

import crud.models.Entity
import book.model.BookTable
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

class ReviewTable(tag:Tag) extends Entity[ReviewPersistenceModel](tag, "ReviewS") {
  def content = column[String]("CONTENT")
  def bookId = column[Int]("BOOK_ID")

  def bookFK = foreignKey("BOOK_FK", bookId, TableQuery[BookTable])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  def * = (id, name, content, bookId) <> (ReviewPersistenceModel.tupled, ReviewPersistenceModel.unapply _)
}
