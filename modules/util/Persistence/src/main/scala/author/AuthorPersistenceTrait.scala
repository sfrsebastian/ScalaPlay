package author.persistence

import crud.layers.CrudPersistence
import author.model._
import authorbook.model.{AuthorBookPersistenceModel, AuthorBookTable}
import book.model.MinBookConverter
import book.persistence.BookPersistenceTrait
import editorial.model.Editorial
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

  override implicit def Persistence2Model = PersistenceAuthorConverter

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
        .map(r => Persistence2Model.convertWithRelations(r._1, r._2.map(e=>e).distinct)).headOption
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
        .map(r => Persistence2Model.convertWithRelations(r._1, r._2.map(e=>e).distinct)).toSeq
    }
  }

  override def createAction(element: Author): DBIO[Author] = {
    for{
      created <- super.createAction(element)
      _ <- DBIO.sequence(element.books.map(b => bookPersistence.createAction(MinBookConverter.convertWithRelations(b, Seq(),created::Nil, Editorial(b.editorialId,"","", List())))))
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
}
