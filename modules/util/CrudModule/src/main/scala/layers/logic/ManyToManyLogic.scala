/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package layers.logic

import crud.models.{Entity, Row}
import layers.persistence.{CrudPersistence, ManyToManyPersistence}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Trait genérico que aplica la lógica de negocio de una relación muchos a muchos
  * @tparam S2 Modelo de negocio del origen de la relación
  * @tparam S Modelo de negocio del destino de la relación
  * @tparam T Modelo de persistencia del destino de la relación
  * @tparam K Modelo de tabla del destino de la relación
  */
trait ManyToManyLogic[S2<:Row, S<:Row, T<:Row, K <: Entity[T]]{

  /**
    * La persistencia de la entidad
    * Debe contrar con el trait ManyToManyPersistence
    */
  val persistence: CrudPersistence[S, T, K] with ManyToManyPersistence[S2, S]

  /**
    * Función que retorna la colección de origen de un modelo destino dado.
    * @param destination El modelo destino
    * @return La colección de modelo origen asociada al modelo destino dado
    */
  def inverseRelationMapper(destination:S):Seq[S2]

  /**
    * Función que retorna las entidades destino asociadas a una entidad origen con id dado
    * @param source La entidad origen
    */
  def getResourcesFromSource(source: S2, start:Int = 0, limit:Int = Int.MaxValue):Future[Seq[S]] = {
    persistence.runAction(persistence.getAllAction(persistence.table)).map(s=>s.filter(e => inverseRelationMapper(e).map(_.id).contains(source.id)).slice(start, start + limit))
  }

  /**
    * Función que asocia la entidad origen con la entidad destino dada
    * @param source La entidad origen
    * @param destination La entidad destino
    */
  def associateResourceToSource(source:S2, destination:S) : Future[Option[S]] = {
    persistence.runAction(persistence.associateEntityToSourceAction(source, destination))
  }

  /**
    * Función que desasocia la entidad origen con la entidad destino dada
    * @param source La entidad origen
    * @param destination La entidad destino
    */
  def disassociateResourceFromSource(source:S2, destination:S): Future[Option[S]]= {
    persistence.runAction(persistence.disassociateEntityFromSourceAction(source, destination))
  }
}