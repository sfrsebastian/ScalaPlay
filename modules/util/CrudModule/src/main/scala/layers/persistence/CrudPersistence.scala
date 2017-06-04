/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package layers.persistence

import crud.exceptions.TransactionException
import crud.models.{Entity, ModelConverter, Row}
import play.api.libs.concurrent.Execution.Implicits._
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Query

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Trait genérico que persiste una entidad
  * @tparam S El modelo de negocio de la entidad
  * @tparam T El modelo de persistencia de la entidad
  * @tparam K El modelo de tabla de la entidad
  */
trait CrudPersistence[S<:Row, T<:Row, K <: Entity[T]]{

  /**
    * La base de datos a utilizar
    */
  val db:Database = Database.forConfig("Database")

  /**
    * La tabla de la entidad
    */
  var table:TableQuery[K]

  /**
    * Función que mappea el modelo de tabla a tuplas
    */
  def updateProjection(): K => Product

  /**
    * Función que transforma un modelo de persistencia a tuplas
    * @param element El elemento a transformar
    */
  def updateTransform(element:T): Product

  /**
    * Convertidor de modelo de negocio a modelo de persistencia
    */
  implicit val Model2Persistence:ModelConverter[S,T]

  /**
    * Convierte el modelo de negocio a modelo de persistencia
    * @param s El modelo de negocio
    * @param converter El convertidor a utilizar
    * @return Representación en modelo de persistencia del modelo de negocio dado
    */
  implicit def S2T (s : S)(implicit converter : ModelConverter[S,T]) : T = converter.convert(s)

  /**
    * Convierte el modelo de persistencia a modelo de negocio
    * @param t El modelo de persistencia
    * @param converter El convertidor a utilizar
    * @return Representación en modelo de negocio del modelo de persistencia dado
    */
  implicit def T2S (t : T)(implicit converter : ModelConverter[S,T]) : S = converter.convertInverse(t)

  /**
    * Acción que retorna todas las entidades de la tabla
    * @param query Filtro de la búsqueda
    * @param start Inicio de paginación
    * @param limit Fin de paginación
    */
  def getAllAction(query: Query[K, T, Seq], start:Int = 0, limit:Int = Int.MaxValue) : DBIO[Seq[S]] = {
    query.drop(start).take(limit).result.map(l => l.map(e=>e:S))
  }

  /**
    * Acción que retorna un entidad de la tabla
    * @param query filtro de la búsqueda
    */
  def getAction(query: Query[K, T, Seq]):DBIO[Option[S]] = {
    query.result.headOption.map(l => l.map(e=>e:S))
  }

  /**
    * Acción que crea una entidad en la tabla
    * @param element El elemento a crear
    */
  def createAction(element : S):DBIO[S] = {
    for{
      created <- (table returning table) += element
      queried <- getAction(table.filter(_.id === created.id))
    }yield queried.get
  }

  /**
    * Acción que actualiza una entidad en la tabla
    * @param id El id de la entidad a actualizar
    * @param toUpdate La entidad actualizada
    */
  def updateAction(id:Int, toUpdate:S) : DBIO[Option[S]]

  /**
    * Acción que elimina una entidad de la tabla
    * @param id El id de la entidad a eliminar
    */
  def deleteAction(id:Int):DBIO[Option[S]] = {
    for {
      toDelete <- getAction(table.filter (_.id === id) )
      result <- table.filter(_.id === id).delete
    }yield {
      result match{
        case 1 => toDelete
        case _ => None
      }
    }
  }

  /**
    * Función que ejecuta una acción transaccionalmente
    * @param action La acción a ejecutar
    */
  def runAction[S](action:DBIO[S]): Future[S] = db.run(transactionResult(action.transactionally))

  /**
    * Función que ejecuta una acción transaccionalmente
    * @param action La acción a ejecutar
    */
  def transactionResult[S](action: DBIO[S]) = action.asTry.flatMap({
    case Failure(t: Throwable) => throw TransactionException("Error en transacción", t)
    case Success(result) => DBIO.successful(result)
  })
}