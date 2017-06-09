/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package author.persistence

import author.model._
import authorbook.model.{AuthorBookPersistenceModel, AuthorBookTable}
import book.model.Book
import book.persistence.BookPersistenceTrait
import layers.persistence.{CrudPersistence, ManyToManyPersistence}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

trait AuthorPersistenceTrait extends CrudPersistence[Author, AuthorPersistenceModel,AuthorTable] with ManyToManyPersistence[Book, Author] {

  val bookPersistence:BookPersistenceTrait

  val authorBookTable = TableQuery[AuthorBookTable]

  var table = TableQuery[AuthorTable]

  override implicit val Model2Persistence = AuthorPersistenceConverter

  override val updateProjection: AuthorTable => (Rep[String], Rep[String]) = b => (b.name, b.lastName)

  override def updateTransform(element:AuthorPersistenceModel): (String, String) = {
    (element.name, element.lastName)
  }

  override def getAction(query: Query[AuthorTable, AuthorPersistenceModel, Seq]): DBIO[Option[Author]] = {
    for{
      author <- query
        .joinLeft(
          authorBookTable.join(bookPersistence.table).on(_.bookId === _.id)
        ).on(_.id === _._1.authorId)
        .result
    }yield {
      author
        .groupBy(_._1)
        .map(r=>(r._1, r._2.flatMap(_._2).map(_._2)))
        .map(r => Model2Persistence.convertInverse(r._1, r._2.distinct)).headOption
    }
  }

  override def getAllAction(query: Query[AuthorTable, AuthorPersistenceModel, Seq], start: Int, limit: Int): DBIO[Seq[Author]] = {
    for{
      author <- query
        .joinLeft(
          authorBookTable.join(bookPersistence.table).on(_.bookId === _.id)
        ).on(_.id === _._1.authorId)
        .drop(start).take(limit)
        .result
    }yield {
      author
        .groupBy(_._1)
        .map(r=>(r._1, r._2.flatMap(_._2).map(_._2)))
        .map(r => Model2Persistence.convertInverse(r._1, r._2.distinct)).toSeq
    }
  }

  override def createAction(element: Author): DBIO[Author] = {
    for{
      created <- super.createAction(element)
      _ <- DBIO.sequence(element.books.map(b => associateEntityToSourceAction(b, created)))
      author <- getAction(table.filter(_.id === created.id))
    }yield author.get
  }


  override def updateAction(id: Int, toUpdate: Author): DBIO[Option[Author]] = {
    for {
      result <- table.filter(_.id === id).map(updateProjection).update(updateTransform(toUpdate))
      updated <- getAction(table.filter(_.id === id))
    }yield{
      result match{
        case 1=>updated
        case _=>None
      }
    }
  }

  override def associateEntityToSourceAction(book:Book, author:Author):DBIO[Option[Author]] = {
    for{
      _ <- authorBookTable += AuthorBookPersistenceModel(1,"", book.id, author.id)
      author <- getAction(table.filter(_.id === author.id))
    }yield author
  }

  override def disassociateEntityFromSourceAction(book:Book, author:Author):DBIO[Option[Author]] = {
    for{
      result <- authorBookTable.filter(r=> r.authorId === author.id && r.bookId === book.id).delete
      author <- getAction(table.filter(_.id === author.id))
    }yield {
      result match {
        case 1 => author
        case 0 => None
      }
    }
  }
}
