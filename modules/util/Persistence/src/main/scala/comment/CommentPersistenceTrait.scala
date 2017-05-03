package comment.persistence

import crud.layers.CrudPersistence
import comment.model.{CommentPersistenceModel, CommentTable}
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sfrsebastian on 4/26/17.
  */
trait CommentPersistenceTrait extends CrudPersistence[CommentPersistenceModel,CommentTable] {
  var table = TableQuery[CommentTable]

  override val updateProjection: CommentTable => (Rep[String], Rep[String]) = b => (b.name, b.content)

  override def updateTransform(element:CommentPersistenceModel): (String, String) = {
    (element.name, element.content)
  }

  override def updateAction(id: Int, toUpdate: CommentPersistenceModel): DBIO[Option[CommentPersistenceModel]] = {
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
