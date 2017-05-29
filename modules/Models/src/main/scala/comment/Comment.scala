package comment.model

import book.model.Book
import crud.models.Row

/**
  * Created by sfrsebastian on 5/1/17.
  */
case class Comment(id:Int, name:String, content:String, book:Book) extends Row

