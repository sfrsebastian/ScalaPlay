package author.logic

import author.models.{Author, Authors}
import author.persistence.AuthorPersistenceTrait
import crud.layers.CrudLogic

/**
  * Created by sfrsebastian on 4/26/17.
  */
trait AuthorLogicTrait extends CrudLogic[Author, Authors] {
  val persistence : AuthorPersistenceTrait
}
