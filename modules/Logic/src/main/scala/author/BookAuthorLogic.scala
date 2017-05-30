package author

import author.model.Author
import author.persistence.AuthorPersistenceTrait
import author.traits.BookAuthorLogicTrait
import book.model.Book
import com.google.inject.Inject
import layers.persistence.ManyToManyPersistence

/**
  * Created by sfrsebastian on 5/30/17.
  */
class BookAuthorLogic @Inject() (val persistence:AuthorPersistenceTrait with ManyToManyPersistence[Book, Author]) extends BookAuthorLogicTrait
