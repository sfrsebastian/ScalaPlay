package layers.controllers

import crud.exceptions.ServiceLayerException
import crud.models.{Entity, ModelConverter, Row}
import layers.controllers.mixins.{ErrorHandler, UserHandler}
import layers.logic.CrudLogic
import play.api.libs.json.{Format, Json}
import play.api.mvc.{Action, Controller}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 5/29/17.
  */
trait OneToManyCompositeController[S2<:Row, T2<:Row , K2<:Entity[T2] , D, S<:Row, T<:Row, K<:Entity[T]] extends Controller with UserHandler with ErrorHandler {

  val sourceLogic:CrudLogic[S2,T2,K2]

  val destinationLogic:CrudLogic[S,T,K]

  implicit val formatDetail:Format[D]

  implicit def Detail2Model:ModelConverter[S, D]

  implicit def M2S (m : D)(implicit converter : ModelConverter[S,D]) : S = converter.convertInverse(m)

  implicit def S2M (s: S)(implicit converter : ModelConverter[S,D]):D = converter.convert(s)

  def relationMapper(source:S2):Seq[S]

  def aggregationMapper(destination:S, source:S2):S

  def getResourcesFromSource(sourceId:Int, start:Option[Int], limit:Option[Int]) = Action.async {
    val result = for{
      a <- sourceLogic.get(sourceId)
      _ <- predicate(a.isDefined)(ServiceLayerException("El libro dado no existe"))
    }yield Ok(Json.toJson(relationMapper(a.get).map(e => e:D)))
    result.recover(errorHandler)
  }

  def getResourceFromSource(sourceId:Int, destinationId:Int) = Action.async{
    val result = for{
      a <- sourceLogic.get(sourceId)
      _ <- predicate(a.isDefined)(ServiceLayerException("El libro dado no existe"))
      r <- Future(a.flatMap(b => relationMapper(b).find(_.id == destinationId)))
      _ <- predicate(r.isDefined)(ServiceLayerException("No se encontró el comentario solicitado"))
    }yield Ok(Json.toJson(r.get:D))
    result.recover(errorHandler)
  }

  def createResourceInSource(sourceId:Int) = Action.async(parse.json){request =>
    request.body.validateOpt[D].getOrElse(None) match {
      case Some(x) => {
        val result = for{
          a <- sourceLogic.get(sourceId)
          _ <- predicate(a.isDefined)(ServiceLayerException("El libro dado no existe"))
          b <- destinationLogic.create(aggregationMapper(x, a.get))
          _ <- predicate(b.isDefined)(ServiceLayerException("El comentario no puedo ser creado"))
        }yield Ok(Json.toJson(b.get:D))
        result.recover(errorHandler)
      }
      case None => Future(BadRequest("Error en formato de contenido"))
    }
  }

  def deleteResourceFromSource(sourceId:Int, destinationId:Int) = Action.async{
    val result = for{
      a <- sourceLogic.get(sourceId)
      _ <- predicate(a.isDefined)(ServiceLayerException("El libro dado no existe"))
      b <- destinationLogic.get(destinationId)
      _ <- predicate(b.isDefined)(ServiceLayerException("El comentario dado no existe"))
      _ <- predicate(relationMapper(a.get).map(_.id).contains(destinationId))(new ServiceLayerException("El comentario no se encuentra asociado al libro dado"))
      deleted <- destinationLogic.delete(b.get.id)
      _ <- predicate(deleted.isDefined)(ServiceLayerException("Se presento un error eliminando el comentario del libro"))
    }yield Ok(Json.toJson(deleted.get:D))
    result.recover(errorHandler)
  }
}
