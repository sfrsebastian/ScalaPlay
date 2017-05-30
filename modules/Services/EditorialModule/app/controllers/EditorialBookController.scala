package controllers.editorial

import book.logic.BookLogicTrait
import com.google.inject.Inject
import editorial.logic.EditorialLogicTrait

/**
  * Created by sfrsebastian on 5/15/17.
  */
class EditorialBookController @Inject() (val sourceLogic:EditorialLogicTrait, val destinationLogic:BookLogicTrait) extends EditorialBookControllerTrait
