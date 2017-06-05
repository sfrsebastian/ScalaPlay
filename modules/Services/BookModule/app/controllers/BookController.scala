/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package controllers.book

import book.traits.BookLogicTrait
import com.google.inject.Inject
import controllers.traits.BookControllerTrait

class BookController @Inject()(override val logic:BookLogicTrait) extends BookControllerTrait