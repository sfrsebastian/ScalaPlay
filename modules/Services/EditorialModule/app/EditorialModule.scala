/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package editorial.settings

import book.logic.EditorialBookLogic
import book.traits.EditorialBookLogicTrait
import editorial.logic.EditorialLogic
import editorial.persistence.{EditorialPersistence, EditorialPersistenceTrait}
import editorial.traits.EditorialLogicTrait
import play.api.{Configuration, Environment}
import play.api.inject.Module

class EditorialModule extends Module {
  def bindings(env: Environment, conf: Configuration) = Seq(
    bind[EditorialLogicTrait].to[EditorialLogic],
    bind[EditorialPersistenceTrait].to[EditorialPersistence],
    bind[EditorialBookLogicTrait].to[EditorialBookLogic]
  )
}
