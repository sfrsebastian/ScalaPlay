/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package book.logic

import book.persistence.BookPersistenceTrait
import book.traits.EditorialBookLogicTrait
import com.google.inject.Inject

class EditorialBookLogic @Inject()(val persistence:BookPersistenceTrait) extends EditorialBookLogicTrait
