package controllers.book

import book.logic.BookLogicTrait
import com.google.inject.Inject
import controllers.comment.CommentControllerTrait

class BookController @Inject()(override val logic:BookLogicTrait, override val commentController:CommentControllerTrait) extends BookControllerTrait