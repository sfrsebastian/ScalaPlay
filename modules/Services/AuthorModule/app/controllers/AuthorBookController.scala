/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package controllers.author

import author.traits.AuthorLogicTrait
import book.traits.AuthorBookLogicTrait
import com.google.inject.Inject
import controllers.traits.AuthorBookControllerTrait

class AuthorBookController@Inject()(val sourceLogic:AuthorLogicTrait, val destinationLogic:AuthorBookLogicTrait) extends AuthorBookControllerTrait