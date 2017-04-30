package comment.persistence

import comment.models.{Comment, Comments}
import crud.layers.CrudPersistence
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sfrsebastian on 4/26/17.
  */
trait CommentPersistenceTrait extends CrudPersistence[Comment,Comments] {
  var table = TableQuery[Comments]

  override val updateProjection: Comments => (Rep[String], Rep[String]) = b => (b.name, b.lastName)

  override def updateTransform(element:Comment): (String, String) = {
    (element.name, element.lastName)
  }

  override def updateAction(id: Int, toUpdate: Comment): DBIO[Option[Comment]] = {
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
