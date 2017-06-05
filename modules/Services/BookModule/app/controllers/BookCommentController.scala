/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package controllers.book

import book.traits.BookLogicTrait
import com.google.inject.Inject
import comment.traits.CommentLogicTrait
import controllers.traits.BookCommentControllerTrait

class BookCommentController @Inject() (val sourceLogic:BookLogicTrait, val destinationLogic:CommentLogicTrait) extends BookCommentControllerTrait
