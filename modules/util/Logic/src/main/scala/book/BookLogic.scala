package book.logic

import com.google.inject.Inject
import book.persistence.BookPersistenceTrait

/**
  * Created by sfrsebastian on 4/10/17.
  */
class BookLogic @Inject() (override val persistence: BookPersistenceTrait) extends BookLogicTrait
