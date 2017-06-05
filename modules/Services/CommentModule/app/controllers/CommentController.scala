/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package controllers.comment

import book.traits.BookLogicTrait
import com.google.inject.Inject
import comment.traits.CommentLogicTrait
import controllers.traits.CommentControllerTrait

class CommentController @Inject()(override val logic:CommentLogicTrait, override val bookLogic:BookLogicTrait) extends CommentControllerTrait