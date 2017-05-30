package controllers.book

import author.logic.AuthorLogicTrait
import book.logic.BookLogicTrait
import com.google.inject.Inject

/**
  * Created by sfrsebastian on 5/15/17.
  */
class BookAuthorController @Inject()(val sourceLogic:BookLogicTrait, val destinationLogic:AuthorLogicTrait) extends BookAuthorControllerTrait
