package controllers.book

import javax.inject.Inject

import auth.controllers.AuthController
import auth.models.{WithRole, WithRoles}
import auth.settings.AuthenticationEnvironment
import crud.layers.CrudController
import book.logic.BookLogicTrait
import book.models.{Book, Books}
import com.mohiva.play.silhouette.api.Silhouette
import play.api.libs.json.Json

class BookController @Inject()(override val logic:BookLogicTrait, override val silhouette:Silhouette[AuthenticationEnvironment]) extends CrudController[Book, Books] with AuthController {
  override implicit val format = Json.format[Book]

  override def getAll = SecuredAction(WithRole("admin")).async{implicit request =>
    println(Json.toJson(request.identity.toMin).toString())
    super.getAll.apply(request)
  }
}