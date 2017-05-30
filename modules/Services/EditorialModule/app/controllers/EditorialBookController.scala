package controllers.editorial

import book.traits.{BookLogicTrait, EditorialBookLogicTrait}
import com.google.inject.Inject
import controllers.traits.EditorialBookControllerTrait
import editorial.traits.EditorialLogicTrait

/**
  * Created by sfrsebastian on 5/15/17.
  */
class EditorialBookController @Inject() (val sourceLogic:EditorialLogicTrait, val destinationLogic:EditorialBookLogicTrait) extends EditorialBookControllerTrait
