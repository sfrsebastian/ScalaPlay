package controllers.comment

import book.traits.BookLogicTrait
import com.google.inject.Inject
import comment.traits.CommentLogicTrait
import controllers.traits.CommentControllerTrait

/**
  * Created by sfrsebastian on 4/26/17.
  */
class CommentController @Inject()(override val logic:CommentLogicTrait, override val bookLogic:BookLogicTrait) extends CommentControllerTrait