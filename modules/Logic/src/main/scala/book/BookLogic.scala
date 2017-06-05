/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package book.logic

import com.google.inject.Inject
import book.persistence.BookPersistenceTrait
import book.traits.BookLogicTrait

class BookLogic @Inject() (override val persistence: BookPersistenceTrait) extends BookLogicTrait
