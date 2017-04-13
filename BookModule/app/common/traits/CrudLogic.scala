package common.traits

import common.Entity
import scala.concurrent.Future

trait CrudLogic[T, K <: Entity[T]] {
  val persistence:CrudPersistence[T, K]

  def getAll:Future[Seq[T]]={
    persistence.getAll
  }

  def get(id: Int): Future[Option[T]] = {
    persistence.get(id)
  }

  def create(element:T): Future[Option[T]] = {
    persistence.create(element)
  }

  def update(id: Int, toUpdate: T) : Future[Option[T]] = {
    persistence.update(id, toUpdate)
  }

  def delete(id: Int): Future[Option[T]] = {
    persistence.delete(id)
  }
}
