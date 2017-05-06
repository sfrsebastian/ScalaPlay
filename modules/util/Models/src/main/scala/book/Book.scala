package book.model

import author.model.{Author, AuthorMin}
import crud.models.Row
import comment.model.{Comment, CommentMin}
import editorial.model.{Editorial, EditorialMin}

/**
  * Created by sfrsebastian on 4/30/17.
  */
case class Book(id:Int, name:String, description:String, ISBN:String, image:String, comments:Seq[Comment], authors:Seq[Author], editorial: Option[Editorial]) extends Row