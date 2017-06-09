/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package editorial.persistence

import book.model._
import editorial.model._
import layers.persistence.CrudPersistence
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

trait EditorialPersistenceTrait extends CrudPersistence[Editorial, EditorialPersistenceModel, EditorialTable] {
  var table = TableQuery[EditorialTable]

  val booksTable = TableQuery[BookTable]

  override implicit val Model2Persistence = EditorialPersistenceConverter

  implicit def Book2Persistence (t : BookPersistenceModel) : Book = BookPersistenceConverter.convertInverse(t)

  override val updateProjection: EditorialTable => (Rep[String], Rep[String]) = b => (b.name, b.address)

  override def updateTransform(element:EditorialPersistenceModel): (String, String) = (element.name, element.address)

  override def getAction(query: Query[EditorialTable, EditorialPersistenceModel, Seq]): DBIO[Option[Editorial]] = {
    for{
      editorial <- query.joinLeft(booksTable).on(_.id === _.editorialId).result
    }yield{
      editorial.groupBy(_._1).map(r=> Model2Persistence.convertInverse(r._1, r._2.flatMap(_._2.map(b=>b:Book)))).headOption
    }
  }

  override def getAllAction(query: Query[EditorialTable, EditorialPersistenceModel, Seq], start: Int, limit: Int): DBIO[Seq[Editorial]] = {
    for{
      editorial <- query.joinLeft(booksTable).on(_.id === _.editorialId)
        .drop(start).take(limit).result
    }yield{
      editorial.groupBy(_._1).map(r=> Model2Persistence.convertInverse(r._1, r._2.flatMap(_._2.map(b=>b:Book)))).toSeq
    }
  }

  override def createAction(element: Editorial): DBIO[Editorial] = {
    for{
      created <- super.createAction(element)
      _ <- DBIO.sequence(element.books.map(b => booksTable.filter(_.id === b.id).map(_.editorialId).update(Some(created.id))))
      result <- getAction(table.filter(_.id === created.id))
    }yield result.get
  }

  override def deleteAction(id: Int): DBIO[Option[Editorial]] = {
    for{
      _ <- booksTable.filter(_.editorialId === id).map(_.editorialId).update(null)
      deleted <- super.deleteAction(id)
    }yield deleted
  }

  override def updateAction(id: Int, toUpdate: Editorial): DBIO[Option[Editorial]] = {
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
