package traits

import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._
import play.api.libs.concurrent.Execution.Implicits._

trait CrudPersistence[T, K <: Entity[T]] {
  protected def db: Database = Database.forConfig("mydb")
  protected val table:TableQuery[K]

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