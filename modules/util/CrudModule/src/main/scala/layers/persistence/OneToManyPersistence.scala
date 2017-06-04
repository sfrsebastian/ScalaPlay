/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package layers.persistence

import slick.dbio.DBIO

/**
  * Trait genérico que maneja la persistencia de una relación uno a muchos
  * @tparam S2 El origen de la relación
  * @tparam S El destino de la relación
  */
trait OneToManyPersistence[S2, S] {
  /**
    * Actualiza el origen de la relación destino
    * @param source La entidad origen
    * @param destination La entidad destino
    */
  def updateEntitySourceAction(source: Option[S2], destination:S):DBIO[Option[S]]
}