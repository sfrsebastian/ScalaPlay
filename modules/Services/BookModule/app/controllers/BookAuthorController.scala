package controllers.book

import author.traits.{AuthorLogicTrait, BookAuthorLogicTrait}
import book.traits.BookLogicTrait
import com.google.inject.Inject
import controllers.traits.BookAuthorControllerTrait

/**
  * Created by sfrsebastian on 5/15/17.
  */
class BookAuthorController @Inject()(val sourceLogic:BookLogicTrait, val destinationLogic:BookAuthorLogicTrait) extends BookAuthorControllerTrait
