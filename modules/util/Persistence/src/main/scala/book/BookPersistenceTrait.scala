package book.persistence

import author.model.AuthorTable
import authorbook.model.{AuthorBookPersistenceModel, AuthorBookTable}
import crud.layers.CrudPersistence
import book.model._
import comment.persistence.CommentPersistenceTrait
import editorial.model.EditorialTable
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sfrsebastian on 4/10/17.
  */
trait BookPersistenceTrait extends CrudPersistence[Book, BookPersistenceModel, BookTable] {

  val commentPersistence:CommentPersistenceTrait

  val authorBookTable = TableQuery[AuthorBookTable]

  val authorTable = TableQuery[AuthorTable]

  val editorialTable = TableQuery[EditorialTable]

  var table = TableQuery[BookTable]

  val updateProjection: BookTable => (Rep[String], Rep[String], Rep[String], Rep[String], Rep[Option[Int]]) = b => (b.name, b.description, b.ISBN, b.image, b.editorialId)

  override implicit def Model2Persistence = BookPersistenceConverter

  def updateTransform(element:BookPersistenceModel): (String, String, String, String, Option[Int]) = {
    (element.name, element.description, element.ISBN, element.image, element.editorialId)
  }

  override def getAction(query: Query[BookTable, BookPersistenceModel, Seq]): DBIO[Option[Book]] = {
    for{
      book <- query.joinLeft(commentPersistence.table).on(_.id === _.bookId)
        .join(authorBookTable).on(_._1.id === _.bookId)
        .join(authorTable).on(_._2.authorId === _.id)
        .joinLeft(editorialTable).on(_._1._1._1.editorialId === _.id)
        .result
    }yield {
      book
        .groupBy(_._1._1._1._1)
        .map(r=>(r._1,r._2.map(_._1._1._1._2), r._2.map(_._1._2), r._2.map(_._2)))
        .map(r => BookPersistenceConverter.convertInverse(r._1, r._2.flatMap(e=>e).distinct, r._3.distinct, r._4.head)).headOption
    }
  }

  override def getAllAction(query: Query[BookTable, BookPersistenceModel, Seq], start: Int, limit: Int): DBIO[Seq[Book]] = {
    for{
      book <- query.joinLeft(commentPersistence.table).on(_.id === _.bookId)
        .join(authorBookTable).on(_._1.id === _.bookId)
        .join(authorTable).on(_._2.authorId === _.id)
        .joinLeft(editorialTable).on(_._1._1._1.editorialId === _.id)
        .drop(start).take(limit)
        .result
    }yield {
      book
        .groupBy(_._1._1._1._1)
        .map(r=>(r._1,r._2.map(_._1._1._1._2), r._2.map(_._1._2), r._2.map(_._2)))
        .map(r => BookPersistenceConverter.convertInverse(r._1, r._2.flatMap(e=>e).distinct, r._3.distinct, r._4.head)).toSeq
    }
  }

  override def createAction(element: Book): DBIO[Book] = {
    for{
      created <- super.createAction(element)
      _ <- DBIO.seq(authorBookTable ++= element.authors.map(u => AuthorBookPersistenceModel(1,"",created.id, u.id)))
      _ <- DBIO.sequence(element.comments.map(c => commentPersistence.createAction(c.copy(book = created))))
      book <- getAction(table.filter(_.id === created.id))
    }yield book.get
  }

  override def updateAction(id: Int, toUpdate: Book): DBIO[Option[Book]] = {
    for {
      result <- table.filter(_.id === id).map(updateProjection).update(updateTransform(toUpdate))
      updated <- getAction(table.filter(_.id === id))
    }yield{
      result match{
        case 1=> updated
        case _=> None
      }
    }
  }

  def addBookToAuthorAction(authorId:Int, bookId:Int):DBIO[Option[Book]] = {
    for{
      _ <- authorBookTable += AuthorBookPersistenceModel(1,"", bookId, authorId)
      book <- getAction(table.filter(_.id === bookId))
    }yield book
  }

  def removeBookFromAuthorAction (authorId:Int, bookId:Int):DBIO[Option[Book]] = {
    for{
      result <- authorBookTable.filter(r=> r.authorId === authorId && r.bookId === bookId).delete
      book <- getAction(table.filter(_.id === bookId))
    }yield {
      result match {
        case 1 => book
        case 0 => None
      }
    }
  }

  def replaceBooksFromAuthorAction(authorId:Int, books:Seq[Book]): DBIO[Seq[Book]] = {
    for{
      _ <- authorBookTable.filter(_.authorId === authorId).delete
      result <- DBIO.sequence(books.map(b => addBookToAuthorAction(authorId, b.id)))
    }yield{
      result.flatten
    }
  }
}
