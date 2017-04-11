package logic.bookModule

import models.bookModule.Book
import traits.CrudLogic
import persistence.bookModule.BookPersistenceTrait
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future

trait BookLogicTrait extends CrudLogic[Book] {

  val persistence : BookPersistenceTrait

  override def getAll: Future[Seq[Book]] = {
    persistence.getAll
  }

  override def get(id: Long): Future[Option[Book]] = {
    persistence.get(id)
  }

  override def create(element: Book): Future[Option[Book]] = {
    persistence.create(element)
  }

  override def update(id: Long, toUpdate: Book): Future[Option[Book]] = {
    persistence.update(id, toUpdate).map(_ match{
      case 0 => None
      case _ => Some(toUpdate)
    })
  }

  override def delete(id: Long): Future[Option[Book]] = {
    for {
      book <- persistence.get(id)
      num <- persistence.delete(id)
      if num > 0
    } yield book
  }
}
