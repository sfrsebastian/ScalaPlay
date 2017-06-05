/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package Review.settings

import author.logic.AuthorLogic
import author.persistence.{AuthorPersistence, AuthorPersistenceTrait}
import author.traits.AuthorLogicTrait
import book.logic.BookLogic
import book.persistence.{BookPersistence, BookPersistenceTrait}
import book.traits.BookLogicTrait
import review.logic.ReviewLogic
import review.persistence.{ReviewPersistence, ReviewPersistenceTrait}
import review.traits.ReviewLogicTrait
import play.api.{Configuration, Environment}
import play.api.inject.Module

class ReviewModule extends Module {
  def bindings(env: Environment, conf: Configuration) = Seq(
    bind[ReviewLogicTrait].to[ReviewLogic],
    bind[BookLogicTrait].to[BookLogic],
    bind[BookPersistenceTrait].to[BookPersistence],
    bind[ReviewPersistenceTrait].to[ReviewPersistence],
    bind[AuthorLogicTrait].to[AuthorLogic],
    bind[AuthorPersistenceTrait].to[AuthorPersistence]
  )
}
