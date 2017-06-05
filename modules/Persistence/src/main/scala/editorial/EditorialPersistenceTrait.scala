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

  override def deleteAction(id: Int): DBIO[Option[Editorial]] = {
    for{
      toDelete <- getAction(table.filter(_.id === id))
      _ <- booksTable.filter(_.editorialId === id).map(_.editorialId).update(null)
      result <- table.filter(_.id === id).delete
    }yield{
      result match{
        case 1 => toDelete
        case _ => None
      }
    }
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
