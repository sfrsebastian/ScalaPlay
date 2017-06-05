/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package editorial.logic

import com.google.inject.Inject
import editorial.persistence.EditorialPersistenceTrait
import editorial.traits.EditorialLogicTrait

class EditorialLogic @Inject() (override val persistence: EditorialPersistenceTrait) extends EditorialLogicTrait
