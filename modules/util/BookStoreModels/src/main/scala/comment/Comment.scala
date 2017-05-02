package models.comment

import crud.models.Row
import models.ModelImplicits._

/**
  * Created by sfrsebastian on 5/1/17.
  */
case class Comment(id:Int, name:String, content:String) extends Row