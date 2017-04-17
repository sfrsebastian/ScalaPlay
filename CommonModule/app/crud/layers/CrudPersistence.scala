package common.traits.layers

import common.traits.model.{Entity, Row}
import play.api.libs.concurrent.Execution.Implicits._
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Query

import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/10/17.
  */
trait CrudPersistence[T, K <: Entity[T]]{
  def db:Database = Database.forConfig("Database")

  var table:TableQuery[K]

  def updateProjection: K => Product

  def updateTransform(element:T): Product

  def getAll : Future[Seq[T]] = {
    getAll(table.sortBy(_.id.asc.nullsLast))
  }

  def getAll(query: Query[K, T, Seq]):Future[Seq[T]] = {
    db.run(query.result).map {
      case null => List()
      case x: Seq[T] => x
    }
  }

  def get(id: Int): Future[Option[T]] = {
    get(table.filter(_.id === id))
  }

  def get(query: Query[K, T, Seq]):Future[Option[T]] = {
    db.run(query.result).map(_.headOption)
  }

  def create(element:T): Future[T] = {
    db.run(((table returning table) += element))
  }

  def update(id: Int, toUpdate: T) : Future[Option[T]]

  def delete(id: Int): Future[Option[T]] = {
    for {
      toDelete <- get(id)
      result <- db.run(table.filter(_.id === id).delete)
    } yield {
      result match{
        case 1 => toDelete
        case _ => None
      }
    }
  }
}
