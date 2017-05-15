package author.logic
import author.persistence.AuthorPersistenceTrait
import crud.layers.CrudLogic
import author.model._
import book.model.Book
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/26/17.
  */
trait AuthorLogicTrait extends CrudLogic[Author, AuthorPersistenceModel, AuthorTable] {
  val persistence : AuthorPersistenceTrait

  def getAuthorsFromBook(bookId: Int):Future[Seq[Author]] = {
    persistence.runAction(persistence.getAllAction(persistence.table))
      .map(s=>s.filter(e=>e.books.map(_.id).contains(bookId)))
  }

  def removeAuthorFromBook(bookId:Int, authorId:Int): Future[Option[Author]]= {
    persistence.runAction(persistence.removeAuthorFromBookAction(bookId, authorId))
  }

  def addAuthorToBook(bookId:Int, authorId:Int) : Future[Option[Author]] = {
    persistence.runAction(persistence.addAuthorToBookAction(bookId,authorId))
  }

  def replaceAuthorsFromBook(bookId:Int, authors:Seq[Author]):Future[Seq[Author]] ={
    persistence.runAction(persistence.replaceAuthorsFromBookAction(bookId, authors))
  }
}
