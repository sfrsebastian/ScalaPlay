/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package layers.controllers

import crud.exceptions.ServiceLayerException
import crud.models.{Entity, ModelConverter, Row}
import layers.controllers.mixins.{ErrorHandler, UserHandler}
import layers.logic.{CrudLogic, ManyToManyLogic}
import play.api.libs.json.{Format, Json}
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Controlador genérico para el manejo de una relación muchos a muchos
  * @tparam S2 El modelo de negocio de la relación origen
  * @tparam T2 El modelo de persistencia de la relación origen
  * @tparam K2 El modelo de tabla de la relación origen
  * @tparam D El modelo detalle de la relación destino
  * @tparam S El modelo de negocio de la relación destino
  * @tparam T El modelo de persistencia de la relación destino
  * @tparam K El modelo de tabla de la relación destino
  */
trait ManyToManyController[S2<:Row, T2<:Row , K2<:Entity[T2] , D, S<:Row, T<:Row, K<:Entity[T]] extends Controller with UserHandler with ErrorHandler{

  /**
    * La lógica del modelo de origen
    */
  val sourceLogic:CrudLogic[S2, T2, K2]

  /**
    * La lógica del modelo destino
    * Debe contar con el trait ManyToManyLogic
    */
  val destinationLogic:CrudLogic[S, T, K] with ManyToManyLogic[S2, S, T, K]

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
    * Función que retorna la colección del modelo destino según un modelo origen dado.
    * @param source El modelo origen
    * @return La colección de modelo destino asociado al modelo origen dado
    */
  def relationMapper(source:S2):Seq[S]

  /**
    * Función que retorna la colección del modelo origen según un modelo destino dado.
    * @param destination El modelo destino
    * @return La colección de modelo origen asociado al modelo destino dado
    */
  def inverseRelationMapper(destination:S):Seq[S2]

  /**
    * Mensaje cuando no se encuentra el origen de la relación
    */
  val originNotFound:String = "El origen dado no existe"

  /**
    * Mensaje cuando no se encuentra el destino de la relación
    */
  val destinationNotFound:String = "El destino solicitado no existe"

  /**
    * Mensaje cuando una entidad destino no se encuentra asociada a una entidad origen
    */
  val destinationNotAssociated:String = "El destino no se encuentra asociado al origen dado"

  /**
    * Mensaje cuando una no se asocia una entidad destino con una entidad origen
    */
  val errorAssociatingDestination:String = "Se presento un error asociando el destino al origen"

  /**
    * Mensaje cuando no se elimina una entidad destino de una entidad origen
    */
  val errorDisassociatingDestination:String = "Se presento un error desasociando el destino del origen"

  /**
    * Servicio que retorna las entidades destino asociadas a una entidad origen con id dado
    * @param sourceId Identificador de la entidad origen
    */
  def getResourcesFromSource(sourceId:Int, start:Option[Int], limit:Option[Int]): Action[AnyContent] = Action.async {
    val result = for{
      a <- sourceLogic.get(sourceId)
      _ <- predicate(a.isDefined)(ServiceLayerException(originNotFound))
      r <- destinationLogic.getResourcesFromSource(a.get, start.getOrElse(0), limit.getOrElse(Int.MaxValue))
    }yield Ok(Json.toJson(r.map(e=>e:D)))
    result.recover(errorHandler)
  }

  /**
    * Servicio que retorna la entidad destino con id dado asociada a una entidad origen con id dado
    * @param sourceId Identificador de la entidad de origen
    * @param destinationId Identificador de la entidad destino
    */
  def getResourceFromSource(sourceId:Int, destinationId:Int): Action[AnyContent] = Action.async{
    val result = for {
      a <- sourceLogic.get(sourceId)
      _ <- predicate(a.isDefined)(ServiceLayerException(originNotFound))
      b <- destinationLogic.get(destinationId)
      _ <- predicate(b.isDefined)(ServiceLayerException(destinationNotFound))
      _ <- predicate(inverseRelationMapper(b.get).map(_.id).contains(sourceId))(ServiceLayerException(destinationNotAssociated))
    }yield Ok(Json.toJson(b.get: D))
    result.recover(errorHandler)
  }

  /**
    * Servicio que asocia una entidad origen con una entidad destino
    * @param sourceId Identificador de la entidad de origen
    * @param destinationId Identificador de la entidad destino
    */
  def associateResourceToSource(sourceId:Int, destinationId:Int): Action[AnyContent] = Action.async{
    val result = for {
      a <- sourceLogic.get(sourceId)
      _ <- predicate(a.isDefined)(ServiceLayerException(originNotFound))
      b <- destinationLogic.get(destinationId)
      _ <- predicate(b.isDefined)(ServiceLayerException(destinationNotFound))
      c <- destinationLogic.associateResourceToSource(a.get, b.get)
      _ <- predicate(c.isDefined)(ServiceLayerException(errorAssociatingDestination))
    }yield Ok(Json.toJson(c.get:D))
    result.recover(errorHandler)
  }

  /**
    * Servicio que elimina una entidad destino de de una entidad origen
    * @param sourceId Identificador de la entidad de origen
    * @param destinationId Identificador de la entidad destino
    */
  def disassociateResourceFromSource(sourceId:Int, destinationId:Int): Action[AnyContent] = Action.async{
    val result = for{
      a <- sourceLogic.get(sourceId)
      _ <- predicate(a.isDefined)(ServiceLayerException(originNotFound))
      b <- destinationLogic.get(destinationId)
      _ <- predicate(b.isDefined)(ServiceLayerException(destinationNotFound))
      _ <- predicate(relationMapper(a.get).map(_.id).contains(destinationId))(ServiceLayerException(destinationNotAssociated))
      deleted <- destinationLogic.disassociateResourceFromSource(a.get, b.get)
      _ <- predicate(deleted.isDefined)(ServiceLayerException(errorDisassociatingDestination))
    }yield Ok(Json.toJson(deleted.get:D))
    result.recover(errorHandler)
  }
}