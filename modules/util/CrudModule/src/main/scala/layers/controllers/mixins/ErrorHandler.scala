/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package layers.controllers.mixins

import crud.exceptions.{LogicLayerException, ServiceLayerException, TransactionException}
import play.api.mvc.Result
import play.api.mvc.Results._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Define acciones de manejo de errores en la capa de servicios
  */
trait ErrorHandler{
  /**
    * @param condition La condición que se debería cumplir
    * @param fail La excepción a crear en caso de que falle el predicado
    */
  def predicate(condition: Boolean)(fail: Exception): Future[Unit] = {
    if (condition)
      Future( () )
    else Future.failed(fail)
  }

  /**
    * Manejo de las excepciones en recover de un Future
    */
  def errorHandler: PartialFunction[Throwable, Result] = {
    case e: TransactionException => InternalServerError(e.message)
    case e: ServiceLayerException => BadRequest(e.getMessage)
    case e: LogicLayerException => BadRequest(e.getMessage)
    case _ => InternalServerError("Se presento un error en el servidor")
  }
}