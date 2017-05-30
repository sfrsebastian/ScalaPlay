package controllers.book

import book.model.BookMin
import book.traits.BookLogicTrait
import com.google.inject.Inject
import comment.model._
import comment.traits.CommentLogicTrait
import controllers.traits.BookCommentControllerTrait
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 5/15/17.
  */
class BookCommentController @Inject() (val sourceLogic:BookLogicTrait, val destinationLogic:CommentLogicTrait) extends BookCommentControllerTrait
