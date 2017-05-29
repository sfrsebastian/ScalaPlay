package author.model

import crud.models.Row

/**
  * Created by sfrsebastian on 4/30/17.
  */
case class AuthorPersistenceModel(id:Int, name:String, lastName:String) extends Row
