package book.settings

import author.BookAuthorLogic
import author.traits.BookAuthorLogicTrait
import book.logic.BookLogic
import book.persistence.{BookPersistence, BookPersistenceTrait}
import book.traits.BookLogicTrait
import comment.logic.CommentLogic
import comment.persistence.{CommentPersistence, CommentPersistenceTrait}
import comment.traits.CommentLogicTrait
import play.api.{Configuration, Environment}
import play.api.inject.Module

class BookModule extends Module {
  def bindings(env: Environment, conf: Configuration) = Seq(
    bind[BookLogicTrait].to[BookLogic],
    bind[BookAuthorLogicTrait].to[BookAuthorLogic],
    bind[BookPersistenceTrait].to[BookPersistence],
    bind[CommentPersistenceTrait].to[CommentPersistence],
    bind[CommentLogicTrait].to[CommentLogic]
  )
}
