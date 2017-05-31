package crud.exceptions

/**
  * Created by sfrsebastian on 5/30/17.
  */
case class LogicLayerException (message: String = "", cause: Throwable = None.orNull) extends Exception(message, cause)
