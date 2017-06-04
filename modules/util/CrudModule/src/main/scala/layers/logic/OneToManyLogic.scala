/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package layers.logic

import crud.models.{Entity, Row}
import layers.persistence.{CrudPersistence, OneToManyPersistence}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Trait genérico que aplica la lógica de negocio de una relación uno a muchos
  * @tparam S2 Modelo de negocio del origen de la relación
  * @tparam S Modelo de negocio del destino de la relación
  * @tparam T Modelo de persistencia del destino de la relación
  * @tparam K Modelo de tabla del destino de la relación
  */
trait OneToManyLogic[S2<:Row, S<:Row, T<:Row, K <: Entity[T]] {

  /**
    * La persistencia de la entidad
    * Debe contrar con el trait OneToManyPersistence
    */
  val persistence:CrudPersistence[S, T, K] with OneToManyPersistence[S2,S]

  /**
    * Función que retorna la entidad de origen de un modelo destino dado.
    * @param destination El modelo destino
    * @return La colección de modelo origen asociada al modelo destino dado
    */
  def inverseOneToManyRelationMapper(destination:S):Option[S2]

  /**
    * Función que retorna las entidades destino asociadas a una entidad origen con id dado
    * Si se manejan muchos registros, sobreescribir con uso de predicado en getAllAction
    * @param source La entidad origen
    * @param start El inicio de paginación
    * @param limit El fin de paginación
    */
  def getResourcesFromSource(source: S2, start:Int = 0, limit:Int = Int.MaxValue):Future[Seq[S]] = {
    persistence.runAction(persistence.getAllAction(persistence.table))
      .map(s=>s.filter(e => inverseOneToManyRelationMapper(e).map(_.id).contains(source.id)).slice(start, start + limit))
  }

  /**
    * Función que actualiza la relación en el destino dado
    * @param source La entidad origen
    * @param destination La entidad destino
    */
  def updateResourceToSourceRelation(source:Option[S2], destination:S):Future[Option[S]] = {
    persistence.runAction(persistence.updateEntitySourceAction(source, destination))
  }
}