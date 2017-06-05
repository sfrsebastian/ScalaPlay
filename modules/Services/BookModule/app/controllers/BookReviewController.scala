/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package controllers.book

import book.traits.BookLogicTrait
import com.google.inject.Inject
import review.traits.ReviewLogicTrait
import controllers.traits.BookReviewControllerTrait

class BookReviewController @Inject() (val sourceLogic:BookLogicTrait, val destinationLogic:ReviewLogicTrait) extends BookReviewControllerTrait
