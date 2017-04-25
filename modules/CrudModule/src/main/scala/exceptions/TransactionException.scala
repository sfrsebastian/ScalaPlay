package crud.exceptions

/**
  * Created by sfrsebastian on 4/24/17.
  */
case class TransactionException(message: String = "", cause: Throwable = None.orNull) extends Exception(message, cause)