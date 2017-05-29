package editorial.logic

import com.google.inject.Inject
import editorial.persistence.EditorialPersistenceTrait

/**
  * Created by sfrsebastian on 4/26/17.
  */
class EditorialLogic @Inject() (override val persistence: EditorialPersistenceTrait) extends EditorialLogicTrait
