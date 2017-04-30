package editorial.logic

import editorial.persistence.EditorialPersistenceTrait
import com.google.inject.Inject

import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/26/17.
  */
class EditorialLogic @Inject() (override val persistence: EditorialPersistenceTrait) extends EditorialLogicTrait
