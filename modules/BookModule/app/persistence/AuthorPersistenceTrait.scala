package author.persistence

import author.models.{Author, Authors}
import crud.layers.CrudPersistence
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sfrsebastian on 4/26/17.
  */
trait AuthorPersistenceTrait extends CrudPersistence[Author,Authors] {
  var table = TableQuery[Authors]

  override val updateProjection: Authors => (Rep[String], Rep[String]) = b => (b.name, b.lastName)

  override def updateTransform(element:Author): (String, String) = {
    (element.name, element.lastName)
  }

  override def updateAction(id: Int, toUpdate: Author): DBIO[Option[Author]] = {
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
