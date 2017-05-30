package book.logic

import book.persistence.BookPersistenceTrait
import book.traits.EditorialBookLogicTrait
import com.google.inject.Inject

/**
  * Created by sfrsebastian on 5/30/17.
  */
class EditorialBookLogic @Inject()(val persistence:BookPersistenceTrait) extends EditorialBookLogicTrait
