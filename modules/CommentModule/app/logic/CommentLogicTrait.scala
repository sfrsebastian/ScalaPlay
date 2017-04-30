package comment.logic

import comment.models.{Comment, Comments}
import comment.persistence.CommentPersistenceTrait
import crud.layers.CrudLogic

/**
  * Created by sfrsebastian on 4/26/17.
  */
trait CommentLogicTrait extends CrudLogic[Comment, Comments] {
  val persistence : CommentPersistenceTrait
}
