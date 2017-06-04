/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package layers.controllers

import crud.models.{Entity, ModelConverter, Row}
import layers.controllers.mixins._
import layers.logic.CrudLogic
import play.api.libs.json.{Format, JsValue, Json}
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Controlador genérico con servicios Crud de una entidad
  * @tparam D Modelo detalle de la entidad
  * @tparam S Modelo de negocio de la entidad
  * @tparam T Modelo de persistencia de la entidad
  * @tparam K Modelo de tabla de la entidad
  */
trait CrudController[D, S<:Row, T<:Row , K <: Entity[T]] extends Controller with UserHandler with ErrorHandler{

  /**
    * La lógica de la entidad
    */
  val logic:CrudLogic[S, T, K]

  /**
    * El formato de tipo detalle de la entidad
    */
  implicit val formatDetail:Format[D]

  /**
    * Convertidor de modelo de negocio a modelo detalle
    */
  implicit val Model2Detail:ModelConverter[S, D]

  /**
    * Convierte el modelo detalle a modelo de negocio
    * @param d El modelo detalle
    * @param converter El convertidor a utilizar
    * @return Representación en modelo de negocio del modelo detalle dado
    */
  implicit def D2S (d : D)(implicit converter : ModelConverter[S,D]) : S = converter.convertInverse(d)

  /**
    * Convierte el modelo de negocio a modelo de detalle
    * @param s El modelo negocio
    * @param converter El convertidor a utilizar
    * @return Representación en modelo de detalle del modelo negocio dado
    */
  implicit def S2D (s: S)(implicit converter : ModelConverter[S,D]):D = converter.convert(s)

  /**
    * Servicio que retorna todas las entidades
    * @param start Opcional de inicio de paginación
    * @param limit Opcional de fin de paginación
    */
  def getAll(start:Option[Int], limit:Option[Int]): Action[AnyContent] = Action.async{
    //Se convierten las entidades retornadas por la logica
    logic.getAll(start.getOrElse(0), limit.getOrElse(Int.MaxValue)).map(elements => Ok(Json.toJson(elements.map(e=>e:D))))
  }

  /**
    * Servicio que retorna la entidad con id dado
    * @param id Identificador de la entidad solicitada
    */
  def get(id:Int): Action[AnyContent] = Action.async{
    logic.get(id).map {
      //Si no existe la entidad se envia un error 400
      case None => BadRequest("El recurso no existe")
      //Si existe se envia una respuesta con codigo 200
      case Some(element) => Ok(Json.toJson(element: D))
    }
  }

  /**
    * Servicio que crea una entidad
    */
  def create(): Action[JsValue] = Action.async(parse.json){ request =>
    //Se solicita el cuerpo de la petición y se formatea al modelo detalle
    request.body.validateOpt[D].getOrElse(None) match {
      //Si el formato es correcto se crea la entidad
      case Some(x) => logic.create(x:S).map{
        //Si el objeto fue creado se envia su representación en modelo detalle
        case Some(element) => Created(Json.toJson(element:D))
        //Si el objeto no fue creado se envia mensaje de error
        case None => BadRequest("El recurso no pudo ser creado")
      }.recover(errorHandler)
      //Si el formato es incorrecto se envia mensaje de error
      case None => Future(BadRequest("Error en formato de contenido"))
    }
  }

  /**
    * Servicio que actualizad la entidad con id dado
    * @param id Identificador de la entidad a actualizar
    */
  def update(id:Int): Action[JsValue] = Action.async(parse.json) { request =>
    //Se solicita el cuerpo de la petición y se formatea al modelo detalle
    request.body.validateOpt[D].getOrElse(None) match {
      case Some(x) =>
        logic.update(id, x).map {
          //Si el objeto fue actualizado se envia su representación en modelo detalle
          case Some(element) => Ok(Json.toJson(element: D))
          //Si el objeto no fue actualizado se envia mensaje de error
          case None => BadRequest("El recurso no pudo ser actualizado")
        }.recover(errorHandler)
      //Si el formato es incorrecto se envia mensaje de error
      case None => Future(BadRequest("Error en formato de contenido"))
    }
  }

  /**
    * Servicio que elimina la entidad con id dado
    * @param id Identificador de la entidad a eliminar
    */
  def delete(id:Int): Action[AnyContent] = Action.async{
    logic.delete(id).map {
      //Si el objeto fue eliminado se envia su representación en modelo detalle
      case Some(x) => Ok(Json.toJson(x: D))
      //Si el objeto no fue eliminado se envia mensaje de error
      case None => BadRequest("El recurso con id dado no existe")
    }.recover(errorHandler)
  }
}