/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package auth.controllers

import auth.models.user.UserMin
import layers.controllers.mixins.UserHandler
import play.api.libs.json.Json
import play.api.mvc.{AnyContent, Request}

/**
  * Implementación de manejo de usuarios
  */
trait AuthUserHandler extends UserHandler {
  implicit  val userMinFormat = Json.format[UserMin]

  /**
    * Solicita un usuario a partir de un tag en la petición http
    * @param request La petición de donde extraer el usuario
    * @return Opcional con la información del usuario
    */
  override def getIdentity(implicit request: Request[AnyContent]): Option[UserMin] = {
    request.tags.get("identity") match {
      case Some(id) => Some(Json.parse(id).as[UserMin])
      case None => None
    }
  }
}
