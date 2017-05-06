package book.model

import crud.models.Row
import comment.model.{Comment, CommentMin}
import model.ModelImplicits._

/**
  * Created by sfrsebastian on 4/30/17.
  */
case class Book(id:Int, name:String, description:String, ISBN:String, image:String, comments:Seq[CommentMin]) extends Row

case class BookMin(id:Int, name:String, description:String, ISBN:String, image:String) extends Row