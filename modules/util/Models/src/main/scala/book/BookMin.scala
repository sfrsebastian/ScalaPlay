package book.model

import author.model.AuthorMin
import comment.model.CommentMin
import editorial.model.EditorialMin

/**
  * Created by sfrsebastian on 5/6/17.
  */
case class BookMin(id:Int, name:String, description:String, ISBN:String, image:String, comments:Seq[CommentMin])

