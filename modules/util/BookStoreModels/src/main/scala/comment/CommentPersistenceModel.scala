package models.comment

import crud.models.Row

/**
  * Created by sfrsebastian on 4/26/17.
  */
case class CommentPersistenceModel(id:Int, name:String, content:String, bookId:Int) extends Row
