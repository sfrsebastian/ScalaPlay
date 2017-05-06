package author.model

import book.model.BookMin
import crud.models.Row

/**
  * Created by sfrsebastian on 5/6/17.
  */
case class AuthorMin(id:Int, name:String,lastName:String, books:Seq[BookMin]) extends Row