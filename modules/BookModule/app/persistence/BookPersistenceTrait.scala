package book.persistence

import crud.layers.CrudPersistence
import book.models.{Book, Books}
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Rep

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sfrsebastian on 4/10/17.
  */
trait BookPersistenceTrait extends CrudPersistence[Book, Books] {
  var table = TableQuery[Books]

  val updateProjection: Books => (Rep[String], Rep[String], Rep[String], Rep[String]) = b => (b.name, b.description, b.ISBN, b.image)

  def updateTransform(element:Book): (String, String, String, String) = {
    (element.name, element.description, element.ISBN, element.image)
  }

  override def updateAction(id: Int, toUpdate: Book): DBIO[Option[Book]] = {
    for {
      result <- table.filter(_.id === id).map(updateProjection).update(updateTransform(toUpdate))
      updated <- getAction(table.filter(_.id === id))
    }yield{
      result match{
        case 1=>updated
        case _=>None
      }
    }
  }
}
