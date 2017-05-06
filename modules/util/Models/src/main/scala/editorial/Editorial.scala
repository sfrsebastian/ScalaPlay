package editorial.model

import book.model.BookMin
import crud.models.Row

/**
  * Created by sfrsebastian on 4/26/17.
  */
case class Editorial(id:Int, name:String, address:String, books: Seq[BookMin]) extends Row

case class EditorialMin(id:Int, name:String, address:String) extends Row