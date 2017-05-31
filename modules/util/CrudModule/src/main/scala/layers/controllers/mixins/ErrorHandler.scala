package layers.controllers.mixins

import crud.exceptions.{LogicLayerException, ServiceLayerException, TransactionException}
import play.api.mvc.Result
import play.api.mvc.Results._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 5/29/17.
  */
trait ErrorHandler{
  def predicate(condition: Boolean)(fail: Exception): Future[Unit] = {
    if (condition)
      Future( () )
    else Future.failed(fail)
  }

  def errorHandler: PartialFunction[Throwable, Result] = {
    case e: TransactionException => InternalServerError(e.message)
    case e: ServiceLayerException => BadRequest(e.getMessage)
    case e: LogicLayerException => BadRequest(e.getMessage)
    case _ => InternalServerError("Se presento un error en el servidor")
  }
}
