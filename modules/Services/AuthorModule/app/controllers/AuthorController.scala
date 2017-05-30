package controllers.author

import author.traits.AuthorLogicTrait
import com.google.inject.Inject
import controllers.traits.AuthorControllerTrait

/**
  * Created by sfrsebastian on 4/26/17.
  */
class AuthorController @Inject()(override val logic:AuthorLogicTrait) extends AuthorControllerTrait