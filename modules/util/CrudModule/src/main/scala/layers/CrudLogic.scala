package crud.layers

import crud.models.{Entity, ModelConverter, Row}
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait CrudLogic[S<:Row, T<:Row, K <: Entity[T]] {

  val persistence:CrudPersistence[T, K]

  implicit def Model2Persistence:ModelConverter[S,T]

  implicit def Persistence2Model:ModelConverter[T,S]

  implicit def S2T (s : S)(implicit converter : ModelConverter[S,T]) : T = converter.convert(s)

  implicit def T2S (t : T)(implicit converter : ModelConverter[T,S]) : S = converter.convert(t)

  def getAll(start:Int = 0, limit:Int = 100):Future[Seq[S]]={
    val query = persistence.table.sortBy(_.id.asc.nullsLast)
    persistence.runAction(persistence.getAllAction(query, start, limit).map(e => e.map(a => a:S)))
  }

  def get(id: Int): Future[Option[S]] = {
    val query = persistence.table.filter(_.id === id)
    persistence.runAction(persistence.getAction(query).map(e=>e.map(a => a:S)))
  }

  def create(element:S): Future[Option[S]] = {
    persistence.runAction(persistence.createAction(element:T)).map(Some(_))
  }

  def update(id: Int, toUpdate: S) : Future[Option[S]] = {
    persistence.runAction(persistence.updateAction(id, toUpdate:T).map(e => e.map(a => a:S)))
  }

  def delete(id: Int): Future[Option[S]] = {
    persistence.runAction(persistence.deleteAction(id).map(e => e.map(a => a:S)))
  }
}
