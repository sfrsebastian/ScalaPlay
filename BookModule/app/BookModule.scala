/**
  * Created by sfrsebastian on 4/10/17.
  */
package settings.bookModule
import logic.bookModule.{BookLogic, BookLogicTrait}
import persistence.bookModule.{BookPersistence, BookPersistenceTrait}
import play.api.{Configuration, Environment}
import play.api.inject.Module

class BookModule extends Module {
  def bindings(env: Environment, conf: Configuration) = Seq(
    bind[BookLogicTrait].to[BookLogic],
    bind[BookPersistenceTrait].to[BookPersistence]
  )
}
