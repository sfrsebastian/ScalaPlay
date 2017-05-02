package book.logic

import crud.layers.CrudLogic
import book.persistence.BookPersistenceTrait
import comment.logic.CommentLogicTrait
import slick.jdbc.PostgresProfile.api._
import models.book._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait BookLogicTrait extends CrudLogic[Book, BookPersistenceModel, BookTable] {
  val persistence : BookPersistenceTrait

  val commentLogic: CommentLogicTrait

  override implicit def Model2Persistence = BookPersistenceConverter

  override implicit def Persistence2Model = PersistenceBookConverter

  override def get(id: Int): Future[Option[Book]] = {
    val action = for{
      book <- persistence.table.filter(_.id === id).joinLeft(commentLogic.persistence.table).on(_.id === _.bookId).sortBy(_._1.id.asc.nullsLast).result
    }yield {
      book.groupBy(_._1).map(r => Persistence2Model.convertCurried(r._1)(r._2.flatMap(_._2.map(i=>i)))).headOption
    }
    persistence.runAction(action)
  }

  override def getAll(start: Int, limit: Int): Future[Seq[Book]] = {
    val action = for{
      books <- persistence.table.joinLeft(commentLogic.persistence.table).on(_.id === _.bookId).sortBy(_._1.id.asc.nullsLast).result
    }yield {
      books.groupBy(_._1).map(r => Persistence2Model.convertCurried(r._1)(r._2.flatMap(_._2.map(i=>i)))).toSeq
    }
    persistence.runAction(action)
  }

  override def create(element: Book): Future[Option[Book]] = {
    val action = for{
      created <- persistence.createAction(element)
      _ <- DBIO.sequence(element.comments.map(c => commentLogic.persistence.createAction(commentLogic.Model2Persistence.convert(c).copy(bookId = created.id))))
      book <- persistence.getAction(persistence.table.filter(_.id === created.id))
    }yield book.map(e=>e:Book)
    persistence.runAction(action)
  }

  override def delete(id: Int): Future[Option[Book]] = {
    val action = for{
      comments <- commentLogic.persistence.getAllAction(commentLogic.persistence.table.filter(_.bookId === id))
      _ <- DBIO.sequence(comments.map(c=>commentLogic.persistence.deleteAction(c.id)))
      book <- persistence.deleteAction(id)
    }yield book.map(e=>e:Book)
    persistence.runAction(action)
  }
}
