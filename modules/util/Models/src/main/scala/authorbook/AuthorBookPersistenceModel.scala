package authorbook.model

import crud.models.Row

/**
  * Created by sfrsebastian on 4/30/17.
  */
case class AuthorBookPersistenceModel(id:Int, name:String, bookId:Int, authorId:Int) extends Row
