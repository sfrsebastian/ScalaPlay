package controllers.bookModule

import models.bookModule.Book
import play.api.libs.json.{Json}
import play.api.mvc._
import logic.bookModule.BookLogic

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object BookController extends Controller {

  implicit val format = Json.format[Book]

  def getAll = Action.async{
    BookLogic.getAll.map(books => Ok(Json.toJson(books)))
  }

  def get(id:Long) = Action.async{
    BookLogic.get(id).map(_ match {
      case None => BadRequest("El libro no existe")
      case Some(book) => Ok(Json.toJson(book))
    })
  }

  def create() = Action.async(parse.json){ request =>
    request.body.validateOpt[Book].getOrElse(None) match {
      case Some(x) => {
        BookLogic.create(x).map(_ match{
          case Some(y) => Ok(Json.toJson(y))
          case None => BadRequest("Error creando nuevo libro")
        })
      }
      case None => Future(BadRequest("Error creando nuevo libro"))
    }
  }

  def update(id:Long) = Action.async(parse.json) { request =>
    request.body.validateOpt[Book].getOrElse(None) match {
      case Some(x) => {
        BookLogic.update(id, x.copy(id=id)).map(_ match{
          case Some(book) => Ok(Json.toJson(book))
          case None => BadRequest("Error actualizando libro con id " + id)
        })
      }
      case None => Future(BadRequest("Error actualizando libro con id " + id))
    }
  }

  def delete(id:Long) = Action.async{
    BookLogic.delete(id).map(_ match{
      case Some(x) => {
        Ok(Json.toJson(x))
      }
      case None => BadRequest("No se encontró libro con id " + id)
    })
  }
}