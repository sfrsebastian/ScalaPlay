package author.settings

import author.logic.{AuthorLogic, AuthorLogicTrait}
import author.persistence.{AuthorPersistence, AuthorPersistenceTrait}
import play.api.{Configuration, Environment}
import play.api.inject.Module

/**
  * Created by sfrsebastian on 4/26/17.
  */
class AuthorModule extends Module {
  def bindings(env: Environment, conf: Configuration) = Seq(
    bind[AuthorLogicTrait].to[AuthorLogic],
    bind[AuthorPersistenceTrait].to[AuthorPersistence]
  )
}
