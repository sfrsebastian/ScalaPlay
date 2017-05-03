package book.model

import crud.models.Row

/**
  * Created by sfrsebastian on 4/30/17.
  */
case class BookPersistenceModel(id:Int, name:String, description:String, ISBN:String, image:String) extends Row
