package models.book

import crud.models.Row
import models.comment.Comment
import models.ModelImplicits._
import uk.co.jemos.podam.common.PodamCollection

/**
  * Created by sfrsebastian on 4/30/17.
  */
case class Book(id:Int, name:String, description:String, ISBN:String, image:String, @PodamCollection(nbrElements = 10) comments:Seq[Comment]) extends Row

case class BookMin(id:Int, name:String, description:String, ISBN:String, image:String) extends Row