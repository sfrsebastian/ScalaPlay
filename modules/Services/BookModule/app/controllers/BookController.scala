package controllers.book

import author.logic.AuthorLogicTrait
import book.logic.BookLogicTrait
import com.google.inject.Inject

class BookController @Inject()(override val logic:BookLogicTrait) extends BookControllerTrait