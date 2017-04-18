package crud.layers

import crud.models.Entity
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait CrudLogic[T, K <: Entity[T]] {
  val persistence:CrudPersistence[T, K]

  def getAll(start:Int = 0, limit:Int = 100):Future[Seq[T]]={
    persistence.getAll(start, limit)
  }

  def get(id: Int): Future[Option[T]] = {
    persistence.get(id)
  }

  def create(element:T): Future[Option[T]] = {
    persistence.create(element).map(Some(_))
  }

  def update(id: Int, toUpdate: T) : Future[Option[T]] = {
    persistence.update(id, toUpdate)
  }

  def delete(id: Int): Future[Option[T]] = {
    persistence.delete(id)
  }
}
