package book.model

import author.model.AuthorMin
import comment.model.CommentMin
import editorial.model.EditorialMin

/**
  * Created by sfrsebastian on 5/6/17.
  */
case class BookDetail(
  val id:Int,
  val name:String,
  val description:String,
  val ISBN:String,
  val image:String,
  val authors:Seq[AuthorMin],
  val comments:Seq[CommentMin],
  val editorial:Option[EditorialMin]
) extends BookDTO