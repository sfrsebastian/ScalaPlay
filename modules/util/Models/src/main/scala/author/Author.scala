package author.model

import crud.models.Row
import book.model.Book

/**
  * Created by sfrsebastian on 4/30/17.
  */
case class Author(id:Int, name:String, lastName:String, books:Seq[Book]) extends Row