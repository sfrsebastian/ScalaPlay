/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package book.traits

import book.model._
import crud.exceptions.LogicLayerException
import layers.logic.CrudLogic
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait BookLogicTrait extends CrudLogic[Book, BookPersistenceModel, BookTable] {

  override def create(element: Book): Future[Option[Book]] = {
    val query = persistence.table.filter(_.ISBN === element.ISBN)
    persistence.runAction(persistence.getAction(query)).flatMap {
      case Some(_) => throw LogicLayerException("El ISBN dado ya existe")
      case None => super.create(element)
    }
  }
}
