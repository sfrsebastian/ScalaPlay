/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package layers.logic

import crud.models.{Entity, Row}
import layers.persistence.CrudPersistence
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Trait genérico que aplica la lógica de negocio de una entidad
  * @tparam S El modelo de negocio de la entidad
  * @tparam T El modelo de persistencia de la entidad
  * @tparam K El modelo de tabla de la entidad
  */
trait CrudLogic[S<:Row, T<:Row, K <: Entity[T]]{

  /**
    * La persistencia de la entidad
    */
  val persistence:CrudPersistence[S, T, K]

  /**
    * Retorna todas las entidades
    * @param start El inicio de paginación
    * @param limit El fin de paginación
    */
  def getAll(start:Int = 0, limit:Int = Int.MaxValue):Future[Seq[S]]={
    persistence.runAction(persistence.getAllAction(persistence.table).map(response=> response))
  }

  /**
    * Retorna la entidad con id dado
    * @param id Identificador de la entidad
    */
  def get(id: Int): Future[Option[S]] = {
    persistence.runAction(persistence.getAction(persistence.table.filter(_.id === id)))
  }

  /**
    * Crea la entidad dada
    * @param element La entidad a crear
    */
  def create(element:S): Future[Option[S]] = {
    persistence.runAction(persistence.createAction(element)).map(e=>Some(e))
  }

  /**
    * Actualiza la entidad con id dado
    * @param id El id de la entidad a actualizar
    * @param toUpdate Modelo actualizado de la entidad
    */
  def update(id: Int, toUpdate: S) : Future[Option[S]] = {
    persistence.runAction(persistence.updateAction(id, toUpdate))
  }

  /**
    * Elimina la entidad con id dado
    * @param id Identificador de la entidad a eliminar
    */
  def delete(id: Int): Future[Option[S]] = {
    persistence.runAction(persistence.deleteAction(id))
  }
}