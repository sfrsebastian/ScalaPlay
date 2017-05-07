package controllers.comment

import com.google.inject.Inject
import comment.logic.CommentLogicTrait

/**
  * Created by sfrsebastian on 4/26/17.
  */
class CommentController @Inject()(override val logic:CommentLogicTrait) extends CommentControllerTrait