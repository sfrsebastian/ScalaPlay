package model

import author.model.Author
import book.model.{Book, BookMin}
import comment.model.{Comment, CommentMin}
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/1/17.
  */
object ModelImplicits {
  implicit def formatBookMin = Json.format[BookMin]
  implicit def formatComment = Json.format[Comment]
  implicit def formatCommentMin = Json.format[CommentMin]
  implicit def formatBook = Json.format[Book]
  implicit def formatAuthor = Json.format[Author]
}
