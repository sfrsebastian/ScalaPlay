package comment.models

import crud.models.{Entity, Row}
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

/**
  * Created by sfrsebastian on 4/26/17.
  */
case class Comment(id:Int, name:String, lastName:String) extends Row

class Comments(tag:Tag) extends Entity[Comment](tag, "COMMENTS") {
  def lastName = column[String]("LASTNAME")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id, name, lastName) <> (Comment.tupled, Comment.unapply _)
}
