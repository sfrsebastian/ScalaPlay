/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package review.logic

import review.persistence.ReviewPersistenceTrait
import com.google.inject.Inject
import review.traits.ReviewLogicTrait

class ReviewLogic @Inject() (override val persistence: ReviewPersistenceTrait) extends ReviewLogicTrait
