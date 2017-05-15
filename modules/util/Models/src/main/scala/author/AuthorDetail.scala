package author

import author.model.AuthorDTO
import book.model.BookMin

/**
  * Created by sfrsebastian on 5/6/17.
  */
case class AuthorDetail(
  val id:Int,
  val name:String,
  val lastName:String,
  val books:Seq[BookMin]
) extends AuthorDTO
