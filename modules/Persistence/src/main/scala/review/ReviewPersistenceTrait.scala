/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package review.persistence

import book.model.BookTable
import review.model._
import layers.persistence.CrudPersistence
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global

trait ReviewPersistenceTrait extends CrudPersistence[Review, ReviewPersistenceModel,ReviewTable] {

  var table = TableQuery[ReviewTable]

  var bookTable = TableQuery[BookTable]

  override implicit val Model2Persistence = ReviewPersistenceConverter

  override val updateProjection: ReviewTable => (Rep[String], Rep[String]) = b => (b.name, b.content)

  override def updateTransform(element:ReviewPersistenceModel): (String, String) = (element.name, element.content)

  override def getAction(query: Query[ReviewTable, ReviewPersistenceModel, Seq]): DBIO[Option[Review]] = {
    for{
      Review <- query.join(bookTable).on(_.bookId === _.id).sortBy(_._1.id.asc.nullsLast).result
    }yield {
      Review.map(r => Model2Persistence.convertInverse(r._1, r._2)).headOption
    }
  }

  override def getAllAction(query: Query[ReviewTable, ReviewPersistenceModel, Seq], start: Int, limit: Int): DBIO[Seq[Review]] = {
    for{
      Reviews <- query.join(bookTable).on(_.bookId === _.id).sortBy(_._1.id.asc.nullsLast)
        .drop(start).take(limit).result
    }yield {
      Reviews.map(r => Model2Persistence.convertInverse(r._1, r._2))
    }
  }

  override def updateAction(id: Int, toUpdate: Review): DBIO[Option[Review]] = {
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

