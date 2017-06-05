/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package book.persistence

import com.google.inject.Inject
import review.persistence.ReviewPersistenceTrait

class BookPersistence @Inject() (val ReviewPersistence: ReviewPersistenceTrait) extends BookPersistenceTrait