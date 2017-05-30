package auth.controllers

import auth.models.user.UserMin
import layers.controllers.mixins.UserHandler
import play.api.libs.json.Json
import play.api.mvc.{AnyContent, Request}

/**
  * Created by sfrsebastian on 4/22/17.
  */
trait AuthUserHandler extends UserHandler {
  implicit  val userMinFormat = Json.format[UserMin]
  override def getIdentity(implicit request: Request[AnyContent]): Option[UserMin] = {
    request.tags.get("identity") match {
      case Some(id) => Some(Json.parse(id).as[UserMin])
      case None => None
    }
  }
}
