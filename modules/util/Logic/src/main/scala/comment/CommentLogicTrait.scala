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

  val allQuery = (bookId:Int) => persistence.table.filter(_.bookId === bookId)

  val singleQuery = (bookId:Int, commentId:Int) => persistence.table.filter(r => r.bookId === bookId && r.id === commentId)

  def getAll(start:Int, limit:Int, bookId:Int)= {
    persistence.runAction(persistence.getAllAction(allQuery(bookId)).map(response=> response))
  }

  def get(bookId:Int, commentId: Int) = {
    persistence.runAction(persistence.getAction(singleQuery(bookId, commentId)))
  }
}