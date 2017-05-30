package comment.logic;

import comment.model._
import layers.logic.CrudLogic
/**
  * Created by sfrsebastian on 4/26/17.
  */
trait CommentLogicTrait extends CrudLogic[Comment, CommentPersistenceModel, CommentTable]