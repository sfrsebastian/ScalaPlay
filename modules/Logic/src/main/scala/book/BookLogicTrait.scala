package book.logic

import author.model.Author
import book.model._
import editorial.model.Editorial
import layers.logic.{CrudLogic, ManyToManyLogic, OneToManyLogic}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

trait BookLogicTrait extends CrudLogic[Book, BookPersistenceModel, BookTable] with ManyToManyLogic[Author, Book, BookPersistenceModel, BookTable] with OneToManyLogic[Editorial, Book, BookPersistenceModel, BookTable] {

  def inverseManyToManyRelationMapper(book:Book):Seq[Author] = book.authors

  def inverseOneToManyRelationMapper(book:Book):Option[Editorial] = book.editorial

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
