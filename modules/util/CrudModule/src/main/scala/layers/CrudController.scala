package crud.layers

import crud.exceptions.TransactionException
import crud.models.{Entity, ModelConverter, Row}
import layers.UserHandler
import play.api.libs.json.{Format, Json}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/12/17.
  */
trait CrudController[F, M, S<:Row, T<:Row , K <: Entity[T]] extends Controller with UserHandler{

  val logic:CrudLogic[S, T, K]

  implicit val formatMin:Format[M]

  implicit val formatForm:Format[F]

  implicit def Min2Model:ModelConverter[S, M]

  implicit def Form2Model:ModelConverter[S, F]

  implicit def M2S (m : M)(implicit converter : ModelConverter[S,M]) : S = converter.convertInverse(m)

  implicit def S2M (s: S)(implicit converter : ModelConverter[S,M]):M = converter.convert(s)

  implicit def F2S (f : F)(implicit converter : ModelConverter[S,F]) : S = converter.convertInverse(f)

  implicit def S2F (s: S)(implicit converter : ModelConverter[S,F]):F = converter.convert(s)

  def getAll(start:Option[Int], limit:Option[Int]) = Action.async{
    logic.getAll(start.getOrElse(0), limit.getOrElse(Int.MaxValue)).map(elements => Ok(Json.toJson(elements.map(e=>e:M))))
  }

  def get(id:Int) = Action.async{
    logic.get(id).map(_ match {
      case None => BadRequest("El recurso no existe")
      case Some(element) => Ok(Json.toJson(element:M))
    })
  }

  def create() = Action.async(parse.json){ request =>
    request.body.validateOpt[F].getOrElse(None) match {
      case Some(x) => logic.create(x:S).map{
        case Some(element) => Created(Json.toJson(element:M))
        case None => BadRequest("El recurso no pudo ser creado")
      }.recover(errorHandler)
      case None => Future(BadRequest("Error en formato de contenido"))
    }
  }

  def update(id:Int) = Action.async(parse.json) { request =>
    request.body.validateOpt[F].getOrElse(None) match {
      case Some(x) => {
        logic.update(id, x).map(_ match{
          case Some(element) => Ok(Json.toJson(element:M))
          case None => BadRequest("El recurso no pudo ser actualizado")
        }).recover(errorHandler)
      }
      case None => Future(BadRequest("Error en formato de contenido"))
    }
  }

  def delete(id:Int) = Action.async{
    logic.delete(id).map(_ match{
      case Some(x) => {
        Ok(Json.toJson(x:M))
      }
      case None => BadRequest("El recurso con id dado no existe")
    }).recover(errorHandler)
  }

  def errorHandler: PartialFunction[Throwable, Result] = {
    case t:TransactionException => InternalServerError(t.message)
    case _ => InternalServerError("Error en servidor, reintentar de nuevo")
  }
}