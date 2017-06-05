/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package comment.model

import crud.models.Entity
import book.model.BookTable
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

class CommentTable(tag:Tag) extends Entity[CommentPersistenceModel](tag, "COMMENTS") {
  def content = column[String]("CONTENT")
  def bookId = column[Int]("BOOK_ID")

  def bookFK = foreignKey("BOOK_FK", bookId, TableQuery[BookTable])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  def * = (id, name, content, bookId) <> (CommentPersistenceModel.tupled, CommentPersistenceModel.unapply _)
}
