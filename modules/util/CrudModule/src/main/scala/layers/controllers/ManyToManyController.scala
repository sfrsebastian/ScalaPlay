package layers.controllers

import crud.exceptions.ServiceLayerException
import crud.models.{Entity, ModelConverter, Row}
import layers.controllers.mixins.{ErrorHandler, UserHandler}
import layers.logic.{CrudLogic, ManyToManyLogic}
import play.api.libs.json.{Format, Json}
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 5/29/17.
  */
trait ManyToManyController[S2<:Row, T2<:Row , K2<:Entity[T2] , D, S<:Row, T<:Row, K<:Entity[T]] extends Controller with UserHandler with ErrorHandler{

  val sourceLogic:CrudLogic[S2, T2, K2]

  val destinationLogic:CrudLogic[S, T, K] with ManyToManyLogic[S2, S, T, K]

  implicit val formatDetail:Format[D]

  implicit def Detail2Model:ModelConverter[S, D]

  implicit def M2S (m : D)(implicit converter : ModelConverter[S,D]) : S = converter.convertInverse(m)

  implicit def S2M (s: S)(implicit converter : ModelConverter[S,D]):D = converter.convert(s)

  def relationMapper(source:S2):Seq[S]

  def inverseRelationMapper(destination:S):Seq[S2]

  def getResourcesFromSource(sourceId:Int) = Action.async {
    val result = for{
      a <- sourceLogic.get(sourceId)
      _ <- predicate(a.isDefined)(ServiceLayerException("El autor dado no existe"))
      r <- destinationLogic.getResourcesFromSource(a.get)
    }yield Ok(Json.toJson(r.map(e=>e:D)))
    result.recover(errorHandler)
  }

  def getResourceFromSource(sourceId:Int, destinationId:Int) = Action.async{
    val result = for {
      a <- sourceLogic.get(sourceId)
      _ <- predicate(a.isDefined)(ServiceLayerException("El autor dado no existe"))
      b <- destinationLogic.get(destinationId)
      _ <- predicate(b.isDefined)(ServiceLayerException("El libro solicitado no existe"))
      _ <- predicate(inverseRelationMapper(b.get).map(_.id).contains(sourceId))(new ServiceLayerException("El libro no se encuentra asociado al autor dado"))
    }yield Ok(Json.toJson(b.get: D))
    result.recover(errorHandler)
  }

  def addResourceToSource(sourceId:Int, destinationId:Int) = Action.async{
    val result = for {
      a <- sourceLogic.get(sourceId)
      _ <- predicate(a.isDefined)(ServiceLayerException("El autor dado no existe"))
      b <- destinationLogic.get(destinationId)
      _ <- predicate(b.isDefined)(ServiceLayerException("El libro solicitado no existe"))
      c <- destinationLogic.addResourceToSource(a.get, b.get)
    }yield{
      c match {
        case Some(destination) => Ok(Json.toJson(destination:D))
        case None => InternalServerError("Se presento un error asociando el libro al autor")
      }
    }
    result.recover(errorHandler)
  }

  def replaceResourcesFromSource(sourceId:Int) = Action.async(parse.json){request =>
    request.body.validateOpt[Seq[D]].getOrElse(None) match {
      case Some(x) => {
        val result = for{
          a <- sourceLogic.get(sourceId)
          _ <- predicate(a.isDefined)(ServiceLayerException("El autor dado no existe"))
          r <- destinationLogic.replaceResourcesFromSource(a.get, x.map(b=>b:S))
        }yield Ok(Json.toJson(r.map(b => b:D)))
        result.recover(errorHandler)
      }
      case None => Future(BadRequest("Error en formato de contenido"))
    }
  }

  def deleteResourceFromSource(sourceId:Int, destinationId:Int) = Action.async{
    val result = for{
      a <- sourceLogic.get(sourceId)
      _ <- predicate(a.isDefined)(ServiceLayerException("El autor dado no existe"))
      b <- destinationLogic.get(destinationId)
      _ <- predicate(b.isDefined)(ServiceLayerException("El libro solicitado no existe"))
      _ <- predicate(relationMapper(a.get).map(_.id).contains(destinationId))(new ServiceLayerException("El libro no se encuentra asociado al autor dado"))
      deleted <- destinationLogic.removeResourceFromSource(a.get, b.get)
    }yield{
      deleted match {
        case Some(destination) => Ok(Json.toJson(destination:D))
        case None => InternalServerError("Se presento un error eliminando el libro del autor")
      }
    }
    result.recover(errorHandler)
  }
}
