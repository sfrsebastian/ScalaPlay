package crud.layers

import crud.models.{Entity, Row}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait CrudLogic[S<:Row, T<:Row, K <: Entity[T]] {

  val persistence:CrudPersistence[S, T, K]

  def getAll(start:Int = 0, limit:Int = Int.MaxValue):Future[Seq[S]]={
    val query = persistence.table
    persistence.runAction(persistence.getAllAction(query)).map(response=> response)
  }

  def get(id: Int): Future[Option[S]] = {
    val query = persistence.table.filter(_.id === id)
    persistence.runAction(persistence.getAction(query))
  }

  def create(element:S): Future[Option[S]] = {
    persistence.runAction(persistence.createAction(element)).map(e=>Some(e))
  }

  def update(id: Int, toUpdate: S) : Future[Option[S]] = {
    persistence.runAction(persistence.updateAction(id, toUpdate))
  }

  def delete(id: Int): Future[Option[S]] = {
    persistence.runAction(persistence.deleteAction(id))
  }
}
