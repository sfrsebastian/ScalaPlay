package crud.models

/**
  * Created by sfrsebastian on 4/30/17.
  */
trait ModelConverter[S <: Row, T <:Row] {
  implicit def convert(s:S):T
}