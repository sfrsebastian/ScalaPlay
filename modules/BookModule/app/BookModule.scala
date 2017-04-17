package book.settings

import book.logic.{BookLogic, BookLogicTrait}
import book.persistence.{BookPersistence, BookPersistenceTrait}
import play.api.{Configuration, Environment}
import play.api.inject.Module

class BookModule extends Module {
  def bindings(env: Environment, conf: Configuration) = Seq(
    bind[BookLogicTrait].to[BookLogic],
    bind[BookPersistenceTrait].to[BookPersistence]
  )
}
