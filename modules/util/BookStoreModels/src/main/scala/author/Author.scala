package models.author

import crud.models.Row
import models.book.BookMin

/**
  * Created by sfrsebastian on 4/30/17.
  */
case class Author(id:Int, name:String, lastName:String, books:Seq[BookMin]) extends Row

case class AuthorMin(id:Int, name:String,lastName:String) extends Row