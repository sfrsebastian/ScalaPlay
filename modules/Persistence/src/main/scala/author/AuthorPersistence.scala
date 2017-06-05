/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package author.persistence

import book.persistence.BookPersistenceTrait
import com.google.inject.Inject

class AuthorPersistence @Inject() (val bookPersistence: BookPersistenceTrait) extends AuthorPersistenceTrait
