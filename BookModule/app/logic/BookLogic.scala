package logic.bookModule

import models.bookModule.Book
import traits.CrudLogic
import persistence.bookModule.BookPersistence
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 3/21/17.
  */
object BookLogic extends CrudLogic[Book] {

  override def getAll: Future[Seq[Book]] = {
    BookPersistence.getAll
  }

  override def get(id: Long): Future[Option[Book]] = {
    BookPersistence.get(id)
  }

  override def create(element: Book): Future[Option[Book]] = {
    BookPersistence.create(element)
  }

  override def update(id: Long, toUpdate: Book): Future[Option[Book]] = {
    BookPersistence.update(id, toUpdate).map(_ match{
      case 0 => None
      case _ => Some(toUpdate)
    })
  }

  override def delete(id: Long): Future[Option[Book]] = {
    for {
      book <- BookPersistence.get(id)
      num <- BookPersistence.delete(id)
      if num > 0
    } yield book
  }
}
