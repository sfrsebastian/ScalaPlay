package api

import javax.inject.Inject

import controllers.comment.CommentController
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class CommentRouteDelegate @Inject()(controller: CommentController) extends SimpleRouter
{
  override def routes: Routes = {
    case GET(p"/") => controller.getAll(None, None)
    case _ => controller.getAll(None, None)
  }
}