package book.traits

import book.model._
import editorial.model.Editorial
import layers.logic.{CrudLogic, OneToManyLogic}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait BookLogicTrait extends CrudLogic[Book, BookPersistenceModel, BookTable] {

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
