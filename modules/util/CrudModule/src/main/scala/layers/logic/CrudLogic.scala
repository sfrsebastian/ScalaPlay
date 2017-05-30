package layers.logic

import crud.models.{Entity, Row}
import layers.persistence.CrudPersistence
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait CrudLogic[S<:Row, T<:Row, K <: Entity[T]]{

  val persistence:CrudPersistence[S, T, K]

  def getAll(start:Int = 0, limit:Int = Int.MaxValue):Future[Seq[S]]={
    persistence.runAction(persistence.getAllAction(persistence.table).map(response=> response))
  }

  def get(id: Int): Future[Option[S]] = {
    persistence.runAction(persistence.getAction(persistence.table.filter(_.id === id)))
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
