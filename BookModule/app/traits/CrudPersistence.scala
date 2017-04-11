package traits

import play.api.libs.concurrent.Execution.Implicits._
import slick.jdbc.PostgresProfile.api._
import traits.profiles.DatabaseProfile

import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/10/17.
  */
trait CrudPersistence[T, K <: Entity[T]] extends DatabaseProfile{

  def table:TableQuery[K]

  def getAll : Future[Seq[T]] = {
    db.run(table.result).map(_ match {
      case null => List()
      case x : Seq[T] => x
    })
  }

  def get(id: Long): Future[Option[T]] = {
    db.run(table.filter(_.id === id).result.headOption)
  }

  def create(element:T): Future[Option[T]] = {
    db.run(((table returning table) += (element)).map(Some(_)))
  }

  def update(id: Long, toUpdate: T) : Future[Int] = {
    get(id).flatMap(_ match {
      case Some(_) => {
        db.run(table.filter(_.id === id).update(toUpdate))
      }
      case None => Future(0)
    })
  }

  def delete(id: Long): Future[Int] = {
    get(id).flatMap(_ match {
      case Some(_) => {
        db.run(table.filter(_.id === id).delete)
      }
      case None => Future(0)
    })
  }
}
