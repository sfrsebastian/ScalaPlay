package book.logic

import com.google.inject.Inject
import book.persistence.BookPersistenceTrait
import comment.logic.CommentLogicTrait
import book.model.Book
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sfrsebastian on 4/10/17.
  */
class BookLogic @Inject() (override val persistence: BookPersistenceTrait, val commentLogic:CommentLogicTrait) extends BookLogicTrait {

  override def create(element: Book): Future[Option[Book]] = {
    val query = persistence.table.filter(_.ISBN === element.ISBN)
    persistence.runAction(persistence.getAction(query)).flatMap(result => {
      result match{
        case Some(_) => Future(None)
        case None => super.create(element)
      }
    })
  }
}
