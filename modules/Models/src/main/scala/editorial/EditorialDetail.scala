package editorial.model

import book.model.BookMin

/**
  * Created by sfrsebastian on 5/6/17.
  */
case class EditorialDetail(id:Int, name:String, address:String, books:Seq[BookMin]) extends EditorialDTO