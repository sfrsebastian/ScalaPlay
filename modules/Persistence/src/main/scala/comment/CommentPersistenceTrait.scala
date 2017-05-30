package comment.persistence

import book.model.BookTable
import comment.model._
import layers.persistence.CrudPersistence
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sfrsebastian on 4/26/17.
  */
trait CommentPersistenceTrait extends CrudPersistence[Comment, CommentPersistenceModel,CommentTable] {

  var table = TableQuery[CommentTable]

  var bookTable = TableQuery[BookTable]

  override implicit def Model2Persistence = CommentPersistenceConverter

  override val updateProjection: CommentTable => (Rep[String], Rep[String]) = b => (b.name, b.content)

  override def updateTransform(element:CommentPersistenceModel): (String, String) = (element.name, element.content)

  override def getAction(query: Query[CommentTable, CommentPersistenceModel, Seq]): DBIO[Option[Comment]] = {
    for{
      comment <- query.join(bookTable).on(_.bookId === _.id).sortBy(_._1.id.asc.nullsLast).result
    }yield {
      comment.map(r => Model2Persistence.convertInverse(r._1, r._2)).headOption
    }
  }

  override def getAllAction(query: Query[CommentTable, CommentPersistenceModel, Seq], start: Int, limit: Int): DBIO[Seq[Comment]] = {
    for{
      comments <- query.join(bookTable).on(_.bookId === _.id).sortBy(_._1.id.asc.nullsLast)
        .drop(start).take(limit).result
    }yield {
      comments.map(r => Model2Persistence.convertInverse(r._1, r._2))
    }
  }

  override def updateAction(id: Int, toUpdate: Comment): DBIO[Option[Comment]] = {
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
}

