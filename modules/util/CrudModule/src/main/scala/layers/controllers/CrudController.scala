package layers.controllers

import crud.models.{Entity, ModelConverter, Row}
import layers.controllers.mixins._
import layers.logic.CrudLogic
import play.api.libs.json.{Format, Json}
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/12/17.
  */
trait CrudController[D, S<:Row, T<:Row , K <: Entity[T]] extends Controller with UserHandler with ErrorHandler{

  val logic:CrudLogic[S, T, K]

  implicit val formatDetail:Format[D]

  implicit def Detail2Model:ModelConverter[S, D]

  implicit def M2S (m : D)(implicit converter : ModelConverter[S,D]) : S = converter.convertInverse(m)

  implicit def S2M (s: S)(implicit converter : ModelConverter[S,D]):D = converter.convert(s)


  def getAll(start:Option[Int], limit:Option[Int]) = Action.async{
    logic.getAll(start.getOrElse(0), limit.getOrElse(Int.MaxValue)).map(elements => Ok(Json.toJson(elements.map(e=>e:D))))
  }

  def get(id:Int) = Action.async{
    logic.get(id).map(_ match {
      case None => BadRequest("El recurso no existe")
      case Some(element) => Ok(Json.toJson(element:D))
    })
  }

  def create() = Action.async(parse.json){ request =>
    request.body.validateOpt[D].getOrElse(None) match {
      case Some(x) => logic.create(x:S).map{
        case Some(element) => Created(Json.toJson(element:D))
        case None => BadRequest("El recurso no pudo ser creado")
      }.recover(errorHandler)
      case None => Future(BadRequest("Error en formato de contenido"))
    }
  }

  def update(id:Int) = Action.async(parse.json) { request =>
    request.body.validateOpt[D].getOrElse(None) match {
      case Some(x) => {
        logic.update(id, x).map(_ match{
          case Some(element) => Ok(Json.toJson(element:D))
          case None => BadRequest("El recurso no pudo ser actualizado")
        }).recover(errorHandler)
      }
      case None => Future(BadRequest("Error en formato de contenido"))
    }
  }

  def delete(id:Int) = Action.async{
    logic.delete(id).map(_ match{
      case Some(x) => {
        Ok(Json.toJson(x:D))
      }
      case None => BadRequest("El recurso con id dado no existe")
    }).recover(errorHandler)
  }
}