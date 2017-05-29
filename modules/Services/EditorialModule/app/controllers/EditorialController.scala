package controllers.editorial

import com.google.inject.Inject
import editorial.logic.EditorialLogicTrait
/**
  * Created by sfrsebastian on 4/26/17.
  */
class EditorialController @Inject()(override val logic:EditorialLogicTrait) extends EditorialControllerTrait