package controllers.traits

import auth.controllers.AuthUserHandler
import book.model.{Book, BookMin, BookPersistenceModel, BookTable}
import comment.model._
import layers.controllers.OneToManyCompositeController
import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 5/29/17.
  */
trait BookCommentControllerTrait extends OneToManyCompositeController[Book, BookPersistenceModel, BookTable, CommentDetail, Comment, CommentPersistenceModel, CommentTable] with AuthUserHandler{

  implicit val formatBookMin = Json.format[BookMin]

  implicit val formatDetail = Json.format[CommentDetail]

  implicit val Model2Detail = CommentDetailConverter

  def relationMapper(book:Book):Seq[Comment] = book.comments

  def aggregationMapper(destination: Comment, source: Book): Comment = destination.copy(book = source)
}
