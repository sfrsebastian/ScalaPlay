/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */

package author

import author.model.Author
import author.persistence.AuthorPersistenceTrait
import author.traits.BookAuthorLogicTrait
import book.model.Book
import com.google.inject.Inject
import layers.persistence.ManyToManyPersistence

class BookAuthorLogic @Inject() (val persistence:AuthorPersistenceTrait with ManyToManyPersistence[Book, Author]) extends BookAuthorLogicTrait
