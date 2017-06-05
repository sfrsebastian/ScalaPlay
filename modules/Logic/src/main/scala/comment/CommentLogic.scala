/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package comment.logic

import comment.persistence.CommentPersistenceTrait
import com.google.inject.Inject
import comment.traits.CommentLogicTrait

class CommentLogic @Inject() (override val persistence: CommentPersistenceTrait) extends CommentLogicTrait
