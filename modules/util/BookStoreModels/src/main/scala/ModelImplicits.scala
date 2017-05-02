package models

import models.book.Book
import models.comment.Comment
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/1/17.
  */
object ModelImplicits {
  implicit def formatComment = Json.format[Comment]
  implicit def formatBook = Json.format[Book]
}
