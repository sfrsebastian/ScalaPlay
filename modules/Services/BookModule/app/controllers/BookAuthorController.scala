/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package controllers.book

import author.traits.BookAuthorLogicTrait
import book.traits.BookLogicTrait
import com.google.inject.Inject
import controllers.traits.BookAuthorControllerTrait

class BookAuthorController @Inject()(val sourceLogic:BookLogicTrait, val destinationLogic:BookAuthorLogicTrait) extends BookAuthorControllerTrait
