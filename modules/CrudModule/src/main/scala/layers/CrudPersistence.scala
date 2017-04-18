package crud.layers

import crud.models.Entity
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

  def getAll(start:Int, limit: Int) : Future[Seq[T]] = {
    getAll(table.sortBy(_.id.asc.nullsLast), start, limit)
  }

  def getAll(query: Query[K, T, Seq], start:Int = 0, limit:Int = 100):Future[Seq[T]] = {
    db.run(query.drop(start).take(limit).result).map {
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
