package controllers.author

import com.google.inject.Inject
import author.logic.AuthorLogicTrait

/**
  * Created by sfrsebastian on 4/26/17.
  */
class AuthorController @Inject()(override val logic:AuthorLogicTrait) extends AuthorControllerTrait