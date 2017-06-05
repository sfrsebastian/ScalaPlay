/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package editorial.traits

import editorial.model.{Editorial, EditorialPersistenceModel, EditorialTable}
import layers.logic.CrudLogic

trait EditorialLogicTrait extends CrudLogic[Editorial, EditorialPersistenceModel, EditorialTable]