package editorial.persistence

import editorial.models.{Editorial, Editorials}
import crud.layers.CrudPersistence
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sfrsebastian on 4/26/17.
  */
trait EditorialPersistenceTrait extends CrudPersistence[Editorial,Editorials] {
  var table = TableQuery[Editorials]

  override val updateProjection: Editorials => (Rep[String], Rep[String]) = b => (b.name, b.lastName)

  override def updateTransform(element:Editorial): (String, String) = {
    (element.name, element.lastName)
  }

  override def updateAction(id: Int, toUpdate: Editorial): DBIO[Option[Editorial]] = {
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
