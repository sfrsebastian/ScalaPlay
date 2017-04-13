package common.traits

import common.Entity
import play.api.libs.json.{Format, Json}
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/12/17.
  */
trait CrudController[T, K <: Entity[T]] extends Controller {

  val logic:CrudLogic[T, K]

  implicit val format:Format[T]

  def getAll = Action.async{
    logic.getAll.map(elements => Ok(Json.toJson(elements)))
  }

  def get(id:Int) = Action.async{
    logic.get(id).map(_ match {
      case None => BadRequest("El elemento no existe")
      case Some(elemento) => Ok(Json.toJson(elemento))
    })
  }

  def create() = Action.async(parse.json){ request =>
    request.body.validateOpt[T].getOrElse(None) match {
      case Some(x) => {
        logic.create(x).map(_ match{
          case Some(y) => Created(Json.toJson(y))
          case None => BadRequest("El elemento no pudo ser creado")
        })
      }
      case None => Future(BadRequest("Error en formato de contenido"))
    }
  }

  def update(id:Int) = Action.async(parse.json) { request =>
    request.body.validateOpt[T].getOrElse(None) match {
      case Some(x) => {
        logic.update(id, x).map(_ match{
          case Some(elemento) => Ok(Json.toJson(elemento))
          case None => BadRequest("El elemento no pudo ser actualizado")
        })
      }
      case None => Future(BadRequest("Error en formato de contenido"))
    }
  }

  def delete(id:Int) = Action.async{
    logic.delete(id).map(_ match{
      case Some(x) => {
        Ok(Json.toJson(x))
      }
      case None => BadRequest("El elemento con id dado no existe")
    })
  }
}
