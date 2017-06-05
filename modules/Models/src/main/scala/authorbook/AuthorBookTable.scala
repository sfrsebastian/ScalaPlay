/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package authorbook.model

import author.model.AuthorTable
import book.model.BookTable
import crud.models.Entity
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

class AuthorBookTable(tag:Tag) extends Entity[AuthorBookPersistenceModel](tag, "BOOK_AUTHOR") {
  def bookId = column[Int]("BOOK_ID")
  def authorId = column[Int]("AUTHOR_ID")
  def bookFK = foreignKey("BOOK_FK", bookId, TableQuery[BookTable])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  def authorFK = foreignKey("AUTHOR_FK", authorId, TableQuery[AuthorTable])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  def * = (id, name, bookId, authorId) <> (AuthorBookPersistenceModel.tupled, AuthorBookPersistenceModel.unapply _)
}
