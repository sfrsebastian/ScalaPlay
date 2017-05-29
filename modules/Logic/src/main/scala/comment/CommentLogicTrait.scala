package comment.logic;

import comment.persistence.CommentPersistenceTrait
import crud.layers.CrudLogic
import comment.model._
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by sfrsebastian on 4/26/17.
  */
trait CommentLogicTrait extends CrudLogic[Comment, CommentPersistenceModel, CommentTable] {

  val persistence : CommentPersistenceTrait
}