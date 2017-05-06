package author.persistence

import crud.layers.CrudPersistence
import author.model._
import authorbook.model.{AuthorBookPersistenceModel, AuthorBookTable}
import book.model.Book
import book.persistence.BookPersistenceTrait
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sfrsebastian on 4/26/17.
  */
trait AuthorPersistenceTrait extends CrudPersistence[Author, AuthorPersistenceModel,AuthorTable] {

  val bookPersistence:BookPersistenceTrait

  val authorBookTable = TableQuery[AuthorBookTable]

  var table = TableQuery[AuthorTable]

  override implicit def Model2Persistence = AuthorPersistenceConverter

  override val updateProjection: AuthorTable => (Rep[String], Rep[String]) = b => (b.name, b.lastName)

  override def updateTransform(element:AuthorPersistenceModel): (String, String) = {
    (element.name, element.lastName)
  }

  override def getAction(query: Query[AuthorTable, AuthorPersistenceModel, Seq]): DBIO[Option[Author]] = {
    for{
      author <- query.join(authorBookTable).on(_.id === _.authorId)
        .join(bookPersistence.table).on(_._2.bookId === _.id)
        .result
    }yield {
      author
        .groupBy(_._1._1)
        .map(r=>(r._1, r._2.map(_._2)))
        .map(r => Model2Persistence.convertInverse(r._1, r._2.map(e=>e).distinct)).headOption
    }
  }

  override def getAllAction(query: Query[AuthorTable, AuthorPersistenceModel, Seq], start: Int, limit: Int): DBIO[Seq[Author]] = {
    for{
      author <- query.join(authorBookTable).on(_.id === _.authorId)
        .join(bookPersistence.table).on(_._2.bookId === _.id)
        .result
    }yield {
      author
        .groupBy(_._1._1)
        .map(r=>(r._1, r._2.map(_._2)))
        .map(r => Model2Persistence.convertInverse(r._1, r._2.map(e=>e).distinct)).toSeq
    }
  }

  override def createAction(element: Author): DBIO[Author] = {
    for{
      created <- super.createAction(element)
      _ <- DBIO.sequence(element.books.map(b => addBookAction(created.id, b)))
      author <- getAction(table.filter(_.id === created.id))
    }yield author.get
  }

  def addBookAction(id:Int, book:Book):DBIO[Book] = {
    for {
      created <- bookPersistence.createAction(book)
      _ <- authorBookTable += AuthorBookPersistenceModel(1, "", created.id, id)
    }yield created
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
}
