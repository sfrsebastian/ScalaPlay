package controllers.author

import author.traits.AuthorLogicTrait
import book.traits.AuthorBookLogicTrait
import com.google.inject.Inject
import controllers.traits.AuthorBookControllerTrait

/**
  * Created by sfrsebastian on 5/15/17.
  */
class AuthorBookController@Inject()(val sourceLogic:AuthorLogicTrait, val destinationLogic:AuthorBookLogicTrait) extends AuthorBookControllerTrait