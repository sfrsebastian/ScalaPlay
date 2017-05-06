package editorial.persistence

import book.model.{BookTable, PersistenceBookConverter}
import crud.layers.CrudPersistence
import editorial.model._
import slick.jdbc
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sfrsebastian on 4/26/17.
  */
trait EditorialPersistenceTrait extends CrudPersistence[Editorial, EditorialPersistenceModel, EditorialTable] {
  var table = TableQuery[EditorialTable]
  val booksTable = TableQuery[BookTable]

  override implicit def Model2Persistence = EditorialPersistenceConverter

  override implicit def Persistence2Model = PersistenceEditorialConverter

  override val updateProjection: EditorialTable => (Rep[String], Rep[String]) = b => (b.name, b.address)

  override def updateTransform(element:EditorialPersistenceModel): (String, String) = {
    (element.name, element.address)
  }

  override def getAction(query: Query[EditorialTable, EditorialPersistenceModel, Seq]): DBIO[Option[Editorial]] = {
    for{
      editorial <- query.joinLeft(booksTable).on(_.id === _.editorialId).result
    }yield{
      editorial.groupBy(_._1).map(r=> Persistence2Model.convertWithRelations(r._1, r._2.flatMap(_._2.map(b=>PersistenceBookConverter.convert(b))))).headOption
    }
  }

  override def getAllAction(query: Query[EditorialTable, EditorialPersistenceModel, Seq], start: Int, limit: Int): DBIO[Seq[Editorial]] = {
    for{
      editorial <- query.joinLeft(booksTable).on(_.id === _.editorialId).result
    }yield{
      editorial.groupBy(_._1).map(r=> Persistence2Model.convertWithRelations(r._1, r._2.flatMap(_._2.map(b=>PersistenceBookConverter.convert(b))))).toSeq
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
