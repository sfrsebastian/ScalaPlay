package editorial.logic

import editorial.models.{Editorial, Editorials}
import editorial.persistence.EditorialPersistenceTrait
import crud.layers.CrudLogic

/**
  * Created by sfrsebastian on 4/26/17.
  */
trait EditorialLogicTrait extends CrudLogic[Editorial, Editorials] {
  val persistence : EditorialPersistenceTrait
}
