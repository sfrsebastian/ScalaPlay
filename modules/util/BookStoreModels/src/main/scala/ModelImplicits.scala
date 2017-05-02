package models

import models.book.{Book, BookMin}
import models.comment.Comment
import models.author._
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/1/17.
  */
object ModelImplicits {
  implicit def formatComment = Json.format[Comment]
  implicit def formatBook = Json.format[Book]
  implicit def formatAuthorMin = Json.format[BookMin]
  implicit def formatAuthor = Json.format[Author]
}
