package controllers.book

import auth.controllers.AuthUserHandler
import crud.layers.CrudController
import book.logic.BookLogicTrait
import book.models.{Book, Books}
import com.google.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent}

import scala.concurrent.ExecutionContext.Implicits.global

class BookController @Inject()(override val logic:BookLogicTrait) extends CrudController[Book, Books] with AuthUserHandler {
  override implicit val format = Json.format[Book]

/*  override def getAll(start:Option[Int], limit:Option[Int]) = Action.async{implicit request =>
    println(getIdentity)
    logic.getAll(start.getOrElse(0), limit.getOrElse(100)).map(elements => Ok(Json.toJson(elements)))
  }

  override def get(id: Int): Action[AnyContent] = Action.async{implicit request =>
    println(getIdentity)
    logic.get(id).map(elements => Ok(Json.toJson(elements)))
  }*/
}