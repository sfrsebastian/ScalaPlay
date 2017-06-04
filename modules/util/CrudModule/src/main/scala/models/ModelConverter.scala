/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package crud.models

/**
  * Trait con la definición de un convertidor entre clases
  * @tparam S El modelo origen
  * @tparam T El modelo destino
  */
trait ModelConverter[S, T] {

  /**
    * Convierte del modelo origen al modelo destino
    * @param source El modelo origen a convertir
    */
  def convert(source:S):T

  /**
    *Convierte del modelo destino al modelo origen
    * @param source El modelo destino a convertir
    */
  def convertInverse(source:T):S
}