package layers.controllers.mixins

import play.api.mvc.{AnyContent, Request}

/**
  * Created by sfrsebastian on 4/22/17.
  */
trait UserHandler {
  def getIdentity(implicit request:Request[AnyContent]): Option[Any]
}
