/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package comment.settings

import author.logic.AuthorLogic
import author.persistence.{AuthorPersistence, AuthorPersistenceTrait}
import author.traits.AuthorLogicTrait
import book.logic.BookLogic
import book.persistence.{BookPersistence, BookPersistenceTrait}
import book.traits.BookLogicTrait
import comment.logic.CommentLogic
import comment.persistence.{CommentPersistence, CommentPersistenceTrait}
import comment.traits.CommentLogicTrait
import play.api.{Configuration, Environment}
import play.api.inject.Module

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
