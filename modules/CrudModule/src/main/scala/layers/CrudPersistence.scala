package crud.layers

import crud.exceptions.TransactionException
import crud.models.Entity
import play.api.libs.concurrent.Execution.Implicits._
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Query
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Created by sfrsebastian on 4/10/17.
  */
trait CrudPersistence[T, K <: Entity[T]]{
  def db:Database = Database.forConfig("Database")

  var table:TableQuery[K]

  def updateProjection: K => Product

  def updateTransform(element:T): Product

  def getAll(start:Int, limit: Int) : Future[Seq[T]] = {
    getAll(table.sortBy(_.id.asc.nullsLast), start, limit)
  }

  def getAll(query: Query[K, T, Seq], start:Int = 0, limit:Int = 100):Future[Seq[T]] = {
    db.run(getAllAction(query, start, limit)).map {
      case null => List()
      case x: Seq[T] => x
    }
  }

  def getAllAction(query: Query[K, T, Seq], start:Int = 0, limit:Int = 100) : DBIO[Seq[T]] = {
    query.drop(start).take(limit).result
  }

  def get(id: Int): Future[Option[T]] = {
    get(table.filter(_.id === id))
  }

  def get(query: Query[K, T, Seq]):Future[Option[T]] = {
    db.run(getAction(query))
  }

  def getAction(query: Query[K, T, Seq]):DBIO[Option[T]] = {
    query.result.headOption
  }

  def create(element:T): Future[T] = {
    db.run(transactionResult(createAction(element).transactionally))
  }

  def createAction(element : T):DBIO[T] = {
    ((table returning table) += element)
  }

  def update(id: Int, toUpdate: T) : Future[Option[T]] = {
    db.run(transactionResult(updateAction(id, toUpdate).transactionally))
  }

  def updateAction(id:Int, toUpdate:T) : DBIO[Option[T]]

  def delete(id: Int): Future[Option[T]] = {
    db.run(transactionResult(deleteAction(id).transactionally))
  }

  def deleteAction(id:Int):DBIO[Option[T]] = {
    for {
      toDelete <- getAction(table.filter (_.id === id) )
      result <- table.filter(_.id === id).delete
      //_ <- DBIO.failed(new Exception("Failed"))
    }yield {
      result match{
        case (1) => toDelete
        case _ => None
      }
    }
  }

  def transactionResult[S](action: DBIO[S]) = action.asTry.flatMap({
    case Failure(t: Throwable) => throw TransactionException("Error en transacción", t)
    case Success(result) => DBIO.successful(result)
  })
}
