package editorial.settings

import book.logic.EditorialBookLogic
import book.traits.EditorialBookLogicTrait
import editorial.logic.EditorialLogic
import editorial.persistence.{EditorialPersistence, EditorialPersistenceTrait}
import editorial.traits.EditorialLogicTrait
import play.api.{Configuration, Environment}
import play.api.inject.Module

/**
  * Created by sfrsebastian on 4/26/17.
  */
class EditorialModule extends Module {
  def bindings(env: Environment, conf: Configuration) = Seq(
    bind[EditorialLogicTrait].to[EditorialLogic],
    bind[EditorialPersistenceTrait].to[EditorialPersistence],
    bind[EditorialBookLogicTrait].to[EditorialBookLogic]
  )
}
