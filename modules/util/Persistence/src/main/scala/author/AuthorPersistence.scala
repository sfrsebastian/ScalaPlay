package author.persistence

import book.persistence.BookPersistenceTrait
import com.google.inject.Inject

/**
  * Created by sfrsebastian on 4/26/17.
  */
class AuthorPersistence @Inject() (val bookPersistence: BookPersistenceTrait) extends AuthorPersistenceTrait
