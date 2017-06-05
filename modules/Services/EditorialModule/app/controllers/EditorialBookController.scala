/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package controllers.editorial

import book.traits.EditorialBookLogicTrait
import com.google.inject.Inject
import controllers.traits.EditorialBookControllerTrait
import editorial.traits.EditorialLogicTrait

class EditorialBookController @Inject() (val sourceLogic:EditorialLogicTrait, val destinationLogic:EditorialBookLogicTrait) extends EditorialBookControllerTrait
