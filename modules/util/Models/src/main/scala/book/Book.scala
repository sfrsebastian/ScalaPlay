package book.model

import author.model.AuthorMin
import crud.models.Row
import comment.model.{Comment, CommentMin}
import editorial.model.EditorialMin

/**
  * Created by sfrsebastian on 4/30/17.
  */
case class Book(id:Int, name:String, description:String, ISBN:String, image:String, comments:Seq[CommentMin], authors:Seq[AuthorMin], editorial:EditorialMin) extends Row

case class BookMin(id:Int, name:String, description:String, ISBN:String, image:String, editorialId:Int) extends Row