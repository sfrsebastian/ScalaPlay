package auth.controllers

import auth.models.{User, UserMin}
import layers.UserHandler
import play.api.libs.json.Json
import play.api.mvc.{AnyContent, Request}

/**
  * Created by sfrsebastian on 4/22/17.
  */
trait AuthUserHandler extends UserHandler {
  override def getIdentity(implicit request: Request[AnyContent]): Option[UserMin] = {
    request.tags.get("identity") match {
      case Some(id) => Some(Json.parse(id).as[UserMin])
      case None => None
    }
  }
}
