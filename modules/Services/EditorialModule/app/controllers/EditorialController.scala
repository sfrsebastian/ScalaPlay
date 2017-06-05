/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package controllers.editorial

import com.google.inject.Inject
import controllers.traits.EditorialControllerTrait
import editorial.traits.EditorialLogicTrait

class EditorialController @Inject()(override val logic:EditorialLogicTrait) extends EditorialControllerTrait