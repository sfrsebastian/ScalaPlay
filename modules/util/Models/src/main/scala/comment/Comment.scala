package comment.model

import book.model.BookMin
import crud.models.Row

/**
  * Created by sfrsebastian on 5/1/17.
  */
case class Comment(id:Int, name:String, content:String, book:BookMin) extends Row

case class CommentMin(id:Int, name:String, content:String) extends Row