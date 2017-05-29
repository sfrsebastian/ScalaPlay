package book.model

import author.model.Author
import crud.models.Row
import comment.model.Comment
import editorial.model.Editorial

/**
  * Created by sfrsebastian on 4/30/17.
  */
case class Book(id:Int, name:String, description:String, ISBN:String, image:String, comments:Seq[Comment], authors:Seq[Author], editorial: Option[Editorial]) extends Row