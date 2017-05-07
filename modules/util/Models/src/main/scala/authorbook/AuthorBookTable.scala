package authorbook.model

import author.model.AuthorTable
import book.model.BookTable
import crud.models.Entity
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

/**
  * Created by sfrsebastian on 4/30/17.
  */
class AuthorBookTable(tag:Tag) extends Entity[AuthorBookPersistenceModel](tag, "BOOK_AUTHOR") {
  def bookId = column[Int]("BOOK_ID")
  def authorId = column[Int]("AUTHOR_ID")
  def bookFK = foreignKey("BOOK_FK", bookId, TableQuery[BookTable])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  def authorFK = foreignKey("AUTHOR_FK", authorId, TableQuery[AuthorTable])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id, name, bookId, authorId) <> (AuthorBookPersistenceModel.tupled, AuthorBookPersistenceModel.unapply _)
}
