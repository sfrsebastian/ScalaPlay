/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package layers.persistence

import slick.dbio.DBIO

/**
  * Trait genérico que maneja la persistencia de una relación muchos a muchos
  * @tparam S2 El origen de la relación
  * @tparam S El destino de la relación
  */
trait ManyToManyPersistence[S2, S] {

  /**
    * Asocia una entidad origen con una entidad destino
    * @param source La entidad origen
    * @param destination La entidad destino
    */
  def associateEntityToSourceAction(source:S2, destination:S):DBIO[Option[S]]

  /**
    * Desasocia una entidad origen de una entidad destino
    * @param source La entidad origen
    * @param destination La entidad destino
    */
  def disassociateEntityFromSourceAction(source:S2, destination:S):DBIO[Option[S]]
}