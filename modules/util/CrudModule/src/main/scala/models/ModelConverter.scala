package crud.models

/**
  * Created by sfrsebastian on 4/30/17.
  */
trait ModelConverter[S, T] {
  def convert(source:S):T
  def convertInverse(source:T):S
}