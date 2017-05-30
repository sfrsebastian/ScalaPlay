package controllers.book

import author.traits.AuthorLogicTrait
import book.traits.BookLogicTrait
import com.google.inject.Inject
import controllers.traits.BookControllerTrait

class BookController @Inject()(override val logic:BookLogicTrait) extends BookControllerTrait