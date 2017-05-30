package controllers.author

import author.logic.AuthorLogicTrait
import book.logic.BookLogicTrait
import com.google.inject.Inject

/**
  * Created by sfrsebastian on 5/15/17.
  */
class AuthorBookController@Inject()(val sourceLogic:AuthorLogicTrait, val destinationLogic:BookLogicTrait) extends AuthorBookControllerTrait