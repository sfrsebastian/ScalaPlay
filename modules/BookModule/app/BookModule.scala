package book.settings

import book.logic.{BookLogic, BookLogicTrait}
import book.persistence.{BookPersistence, BookPersistenceTrait}
import comment.persistence.{CommentPersistence, CommentPersistenceTrait}
import controllers.comment.{CommentController, CommentControllerTrait}
import play.api.{Configuration, Environment}
import play.api.inject.Module

class BookModule extends Module {
  def bindings(env: Environment, conf: Configuration) = Seq(
    bind[BookLogicTrait].to[BookLogic],
    bind[BookPersistenceTrait].to[BookPersistence],
    bind[CommentPersistenceTrait].to[CommentPersistence],
    bind[CommentControllerTrait].to[CommentController]
  )
}
