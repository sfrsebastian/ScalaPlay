package comment.model

import book.model.BookMin

/**
  * Created by sfrsebastian on 5/6/17.
  */
case class CommentDetail(id:Int, content:String, book:BookMin) extends CommentDTO