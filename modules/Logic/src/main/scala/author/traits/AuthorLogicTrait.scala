/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package author.traits

import author.model._
import layers.logic.CrudLogic

trait AuthorLogicTrait extends CrudLogic[Author, AuthorPersistenceModel, AuthorTable]
