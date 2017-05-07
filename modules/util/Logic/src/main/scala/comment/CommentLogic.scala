package comment.logic

import comment.persistence.CommentPersistenceTrait
import com.google.inject.Inject

/**
  * Created by sfrsebastian on 4/26/17.
  */
class CommentLogic @Inject() (override val persistence: CommentPersistenceTrait) extends CommentLogicTrait
