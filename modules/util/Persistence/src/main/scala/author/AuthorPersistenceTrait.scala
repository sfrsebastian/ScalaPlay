package author.persistence

import crud.layers.CrudPersistence
import author.model.{AuthorPersistenceModel, AuthorTable}
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sfrsebastian on 4/26/17.
  */
trait AuthorPersistenceTrait extends CrudPersistence[AuthorPersistenceModel,AuthorTable] {
  var table = TableQuery[AuthorTable]

  override val updateProjection: AuthorTable => (Rep[String], Rep[String]) = b => (b.name, b.lastName)

  override def updateTransform(element:AuthorPersistenceModel): (String, String) = {
    (element.name, element.lastName)
  }

  override def updateAction(id: Int, toUpdate: AuthorPersistenceModel): DBIO[Option[AuthorPersistenceModel]] = {
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
