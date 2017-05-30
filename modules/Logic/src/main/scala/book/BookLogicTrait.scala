package book.logic

import author.model.Author
import book.persistence.BookPersistenceTrait
import book.model._
import layers.logic.{CrudLogic, ManyToManyLogic}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

trait BookLogicTrait extends CrudLogic[Book, BookPersistenceModel, BookTable] with ManyToManyLogic[Author, Book, BookPersistenceModel, BookTable] {

  val persistence : BookPersistenceTrait

  def inverseRelationMapper(book:Book):Seq[Author] = book.authors

  override def create(element: Book): Future[Option[Book]] = {
    val query = persistence.table.filter(_.ISBN === element.ISBN)
    persistence.runAction(persistence.getAction(query)).flatMap(result => {
      result match{
        case Some(_) => Future(None)
        case None => super.create(element)
      }
    })
  }


  def getBooksFromEditorial(editorialId: Int):Future[Seq[Book]] = {
    persistence.runAction(persistence.getAllAction(persistence.table.filter(_.editorialId === editorialId)))
  }

  def addBookToEditorial(editorialId:Int, bookId:Int):Future[Option[Book]] = {
    persistence.runAction(persistence.updateBookEditorialAction(Some(editorialId), bookId))
  }

  def removeBookFromEditorial(editorialId:Int, bookId:Int):Future[Option[Book]] = {
    persistence.runAction(persistence.updateBookEditorialAction(None, bookId))
  }
}
