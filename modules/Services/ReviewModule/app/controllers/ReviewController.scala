/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package controllers.Review

import book.traits.BookLogicTrait
import com.google.inject.Inject
import review.traits.ReviewLogicTrait
import controllers.traits.ReviewControllerTrait

class ReviewController @Inject()(override val logic:ReviewLogicTrait, override val bookLogic:BookLogicTrait) extends ReviewControllerTrait