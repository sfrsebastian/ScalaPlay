/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package controllers.author

import author.traits.AuthorLogicTrait
import com.google.inject.Inject
import controllers.traits.AuthorControllerTrait

class AuthorController @Inject()(override val logic:AuthorLogicTrait) extends AuthorControllerTrait