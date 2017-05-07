package book.model

import comment.model.CommentMin

/**
  * Created by sfrsebastian on 5/6/17.
  */
case class BookMin(id:Int, name:String, description:String, ISBN:String, image:String, comments:Seq[CommentMin])

