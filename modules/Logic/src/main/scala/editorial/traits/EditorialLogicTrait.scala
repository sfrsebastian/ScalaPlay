package editorial.traits

import editorial.model.{Editorial, EditorialPersistenceModel, EditorialTable}
import layers.logic.CrudLogic

/**
  * Created by sfrsebastian on 4/26/17.
  */
trait EditorialLogicTrait extends CrudLogic[Editorial, EditorialPersistenceModel, EditorialTable]