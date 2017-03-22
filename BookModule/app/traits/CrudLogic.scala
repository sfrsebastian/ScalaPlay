package traits

import scala.concurrent.Future

trait CrudLogic[T] {
  def getAll : Future[Seq[T]]

  def get(id: Long): Future[Option[T]]

  def create(element:T): Future[Option[T]]

  def update(id: Long, toUpdate: T) : Future[Option[T]]

  def delete(id: Long): Future[Option[T]]
}
