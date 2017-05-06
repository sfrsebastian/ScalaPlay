package comment.logic;

import comment.persistence.CommentPersistenceTrait
import crud.layers.CrudLogic
import comment.model._
import crud.DatabaseOperations
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
  * Created by sfrsebastian on 4/26/17.
  */
trait CommentLogicTrait extends CrudLogic[Comment, CommentPersistenceModel, CommentTable] {

  val persistence : CommentPersistenceTrait

  def getFromBook(id: Int):Future[Seq[Comment]] = {
    persistence.runAction(persistence.getAllAction(persistence.table.filter(_.bookId === id))).map(s=>s.map(e=>e:Comment))
  }
}
