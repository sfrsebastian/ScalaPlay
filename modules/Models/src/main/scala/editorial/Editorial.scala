package editorial.model

import book.model.Book
import crud.models.Row

/**
  * Created by sfrsebastian on 4/26/17.
  */
case class Editorial(id:Int, name:String, address:String, books: Seq[Book]) extends Row