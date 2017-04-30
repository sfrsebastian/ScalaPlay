package comment.settings

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
    bind[CommentPersistenceTrait].to[CommentPersistence]
  )
}
