/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package book.persistence

import com.google.inject.Inject
import comment.persistence.CommentPersistenceTrait

class BookPersistence @Inject() (val commentPersistence: CommentPersistenceTrait) extends BookPersistenceTrait