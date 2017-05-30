package controllers.editorial

import com.google.inject.Inject
import controllers.traits.EditorialControllerTrait
import editorial.traits.EditorialLogicTrait
/**
  * Created by sfrsebastian on 4/26/17.
  */
class EditorialController @Inject()(override val logic:EditorialLogicTrait) extends EditorialControllerTrait