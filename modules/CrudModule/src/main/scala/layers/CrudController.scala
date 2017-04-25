package crud.layers

import crud.exceptions.TransactionException
import crud.models.Entity
import layers.UserHandler
import play.api.libs.json.{Format, Json}
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/12/17.
  */
trait CrudController[T, K <: Entity[T]] extends Controller with UserHandler{

  val logic:CrudLogic[T, K]

  implicit val format:Format[T]

  def getAll(start:Option[Int], limit:Option[Int]) = Action.async{
    logic.getAll(start.getOrElse(0), limit.getOrElse(100)).map(elements => Ok(Json.toJson(elements)))
  }

  def get(id:Int) = Action.async{
    logic.get(id).map(_ match {
      case None => BadRequest("El recurso no existe")
      case Some(elemento) => Ok(Json.toJson(elemento))
    })
  }

  def create() = Action.async(parse.json){ request =>
    request.body.validateOpt[T].getOrElse(None) match {
      case Some(x) => logic.create(x).map{
        case Some(element) => Created(Json.toJson(element))
        case None => BadRequest("El recurso no pudo ser creado")
      }.recover{
        case t:TransactionException => InternalServerError(t.message)
        case _ => InternalServerError("Error en servidor, reintentar de nuevo")
      }
      case None => Future(BadRequest("Error en formato de contenido"))
    }
  }

  def update(id:Int) = Action.async(parse.json) { request =>
    request.body.validateOpt[T].getOrElse(None) match {
      case Some(x) => {
        logic.update(id, x).map(_ match{
          case Some(elemento) => Ok(Json.toJson(elemento))
          case None => BadRequest("El recurso no pudo ser actualizado")
        }).recover{
          case t:TransactionException => InternalServerError(t.message)
          case _ => InternalServerError("Error en servidor, reintentar de nuevo")
        }
      }
      case None => Future(BadRequest("Error en formato de contenido"))
    }
  }

  def delete(id:Int) = Action.async{
    logic.delete(id).map(_ match{
      case Some(x) => {
        Ok(Json.toJson(x))
      }
      case None => BadRequest("El recurso con id dado no existe")
    }).recover{
      case t:TransactionException => InternalServerError(t.message)
      case _ => InternalServerError("Error en servidor, reintentar de nuevo")
    }
  }
}