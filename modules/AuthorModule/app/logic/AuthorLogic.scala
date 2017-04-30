package author.logic

import author.persistence.AuthorPersistenceTrait
import com.google.inject.Inject

import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/26/17.
  */
class AuthorLogic @Inject() (override val persistence: AuthorPersistenceTrait) extends AuthorLogicTrait
