package book.logic

import book.persistence.BookPersistenceTrait
import crud.layers.CrudLogic
import book.model._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

trait BookLogicTrait extends CrudLogic[Book, BookPersistenceModel, BookTable] {
  val persistence : BookPersistenceTrait

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
