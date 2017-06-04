/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package layers.controllers.mixins

import play.api.mvc.{AnyContent, Request}

/**
  * Define acciones de manejo de usuarios en la capa de servicios
  */
trait UserHandler {
  /**
    * Define la extracción de identidad de un usuario de una petición HTTP
    * @param request La petición de donde extraer el usuario
    * @return Opcional con la información del usuario
    */
  def getIdentity(implicit request:Request[AnyContent]): Option[Any]
}