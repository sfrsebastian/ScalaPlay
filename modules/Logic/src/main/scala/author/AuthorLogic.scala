/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */

package author.logic

import author.persistence.AuthorPersistenceTrait
import author.traits.AuthorLogicTrait
import com.google.inject.Inject

class AuthorLogic @Inject() (override val persistence: AuthorPersistenceTrait) extends AuthorLogicTrait