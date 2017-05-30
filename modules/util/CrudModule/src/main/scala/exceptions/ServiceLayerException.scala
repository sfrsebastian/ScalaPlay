package crud.exceptions

/**
  * Created by sfrsebastian on 5/29/17.
  */
case class ServiceLayerException(message: String = "", cause: Throwable = None.orNull) extends Exception(message, cause)
