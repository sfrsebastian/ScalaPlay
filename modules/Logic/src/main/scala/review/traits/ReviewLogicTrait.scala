/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package review.traits

import review.model._
import layers.logic.CrudLogic

trait ReviewLogicTrait extends CrudLogic[Review, ReviewPersistenceModel, ReviewTable]