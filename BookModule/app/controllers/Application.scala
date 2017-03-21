package controllers.book

import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok("Hello Book")
  }

}