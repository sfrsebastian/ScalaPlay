package comment.model

import crud.models.Row
import model.ModelImplicits._

/**
  * Created by sfrsebastian on 5/1/17.
  */
case class Comment(id:Int, name:String, content:String) extends Row