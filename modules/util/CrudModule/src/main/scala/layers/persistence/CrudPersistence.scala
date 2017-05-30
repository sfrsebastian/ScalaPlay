package layers.persistence

import crud.exceptions.TransactionException
import crud.models.{Entity, ModelConverter, Row}
import play.api.libs.concurrent.Execution.Implicits._
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Query

import scala.util.{Failure, Success}

/**
  * Created by sfrsebastian on 4/10/17.
  */
trait CrudPersistence[S<:Row, T<:Row, K <: Entity[T]]{
  def db:Database = Database.forConfig("Database")

  var table:TableQuery[K]

  def updateProjection: K => Product

  def updateTransform(element:T): Product

  implicit def Model2Persistence:ModelConverter[S,T]

  implicit def S2T (s : S)(implicit converter : ModelConverter[S,T]) : T = converter.convert(s)

  implicit def T2S (t : T)(implicit converter : ModelConverter[S,T]) : S = converter.convertInverse(t)

  def getAllAction(query: Query[K, T, Seq], start:Int = 0, limit:Int = Int.MaxValue) : DBIO[Seq[S]] = {
    query.drop(start).take(limit).result.map(l => l.map(e=>e:S))
  }

  def getAction(query: Query[K, T, Seq]):DBIO[Option[S]] = {
    query.result.headOption.map(l => l.map(e=>e:S))
  }

  def createAction(element : S):DBIO[S] = {
    for{
      created <- (table returning table) += element
      queried <- getAction(table.filter(_.id === created.id))
    }yield queried.get
  }

  def updateAction(id:Int, toUpdate:S) : DBIO[Option[S]]

  def deleteAction(id:Int):DBIO[Option[S]] = {
    for {
      toDelete <- getAction(table.filter (_.id === id) )
      result <- table.filter(_.id === id).delete
      //_ <- DBIO.failed(new Exception("Failed"))
    }yield {
      result match{
        case 1 => toDelete
        case _ => None
      }
    }
  }

  def runAction[S](action:DBIO[S]) = db.run(transactionResult(action.transactionally))

  def transactionResult[S](action: DBIO[S]) = action.asTry.flatMap({
    case Failure(t: Throwable) => throw TransactionException("Error en transacción", t)
    case Success(result) => DBIO.successful(result)
  })
}
