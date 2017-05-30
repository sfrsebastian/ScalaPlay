package layers.controllers

import crud.exceptions.ServiceLayerException
import crud.models.{Entity, ModelConverter, Row}
import layers.controllers.mixins.{ErrorHandler, UserHandler}
import layers.logic.{CrudLogic, OneToManyLogic}
import play.api.libs.json.{Format, Json}
import play.api.mvc.{Action, Controller}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 5/29/17.
  */
trait OneToManyController[S2<:Row, T2<:Row , K2<:Entity[T2] , D, S<:Row, T<:Row, K<:Entity[T]] extends Controller with UserHandler with ErrorHandler {

  val sourceLogic:CrudLogic[S2,T2,K2]

  val destinationLogic:CrudLogic[S,T,K] with OneToManyLogic[S2, S, T, K]

  implicit val formatDetail:Format[D]

  implicit def Detail2Model:ModelConverter[S, D]

  implicit def M2S (m : D)(implicit converter : ModelConverter[S,D]) : S = converter.convertInverse(m)

  implicit def S2M (s: S)(implicit converter : ModelConverter[S,D]):D = converter.convert(s)

  def relationMapper(source:S2):Seq[S]

  def getResourcesFromSource(sourceId:Int, start:Option[Int], limit:Option[Int]) = Action.async {
    val result = for{
      a <- sourceLogic.get(sourceId)
      _ <- predicate(a.isDefined)(ServiceLayerException("La editorial dado no existe"))
      b <- destinationLogic.getResourcesFromSource(a.get, start, limit)
    }yield Ok(Json.toJson(b.map(e => e:D)))
    result.recover(errorHandler)
  }

  def getResourceFromSource(sourceId:Int, destinationId:Int) = Action.async{
    val result = for{
      a <- sourceLogic.get(sourceId)
      _ <- predicate(a.isDefined)(ServiceLayerException("La editorial dado no existe"))
      b <- destinationLogic.get(destinationId)
      _ <- predicate(a.isDefined)(ServiceLayerException("El libro dado no existe"))
      r <- Future(a.flatMap(c => relationMapper(c).find(_.id == destinationId)))
      _ <- predicate(r.isDefined)(ServiceLayerException("El libro dado no esta asociado a la editorial dada"))
    }yield Ok(Json.toJson(b.get:D))
    result.recover(errorHandler)
  }

  def associateResourceInSource(sourceId:Int, destinationId:Int) = Action.async{
    val result = for{
      a <- sourceLogic.get(sourceId)
      _ <- predicate(a.isDefined)(ServiceLayerException("La editorial dada no existe"))
      b <- destinationLogic.get(destinationId)
      _ <- predicate(b.isDefined)(ServiceLayerException("El libro dado no existe"))
      c <- destinationLogic.updateResourceToSourceRelation(a, b.get)
      _ <- predicate(c.isDefined)(ServiceLayerException("Error actualizando asociacion editorial-libro"))
    }yield Ok(Json.toJson(c.get:D))
    result.recover(errorHandler)
  }

  def deleteResourceFromSource(sourceId:Int, destinationId:Int) = Action.async{
    val result = for{
      a <- sourceLogic.get(sourceId)
      _ <- predicate(a.isDefined)(ServiceLayerException("La editorial dada no existe"))
      b <- destinationLogic.get(destinationId)
      _ <- predicate(b.isDefined)(ServiceLayerException("El libro dado no existe"))
      c <- destinationLogic.updateResourceToSourceRelation(None, b.get)
      _ <- predicate(c.isDefined)(ServiceLayerException("Error actualizando asociacion editorial-libro"))
    }yield Ok(Json.toJson(c.get:D))
    result.recover(errorHandler)
  }
}
