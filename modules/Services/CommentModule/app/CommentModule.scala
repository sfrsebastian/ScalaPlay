package comment.settings

import author.logic.{AuthorLogic, AuthorLogicTrait}
import author.persistence.{AuthorPersistence, AuthorPersistenceTrait}
import book.logic.{BookLogic, BookLogicTrait}
import book.persistence.{BookPersistence, BookPersistenceTrait}
import comment.logic.{CommentLogic, CommentLogicTrait}
import comment.persistence.{CommentPersistence, CommentPersistenceTrait}
import play.api.{Configuration, Environment}
import play.api.inject.Module

/**
  * Created by sfrsebastian on 4/26/17.
  */
class CommentModule extends Module {
  def bindings(env: Environment, conf: Configuration) = Seq(
    bind[CommentLogicTrait].to[CommentLogic],
    bind[BookLogicTrait].to[BookLogic],
    bind[BookPersistenceTrait].to[BookPersistence],
    bind[CommentPersistenceTrait].to[CommentPersistence],
    bind[AuthorLogicTrait].to[AuthorLogic],
    bind[AuthorPersistenceTrait].to[AuthorPersistence]
  )
}
