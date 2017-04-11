package logic.bookModule

import com.google.inject.Inject
import persistence.bookModule.BookPersistenceTrait

/**
  * Created by sfrsebastian on 4/10/17.
  */
class BookLogic @Inject() (override val persistence: BookPersistenceTrait) extends BookLogicTrait {

}
