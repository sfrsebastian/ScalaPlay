/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package comment.traits

import comment.model._
import layers.logic.CrudLogic

trait CommentLogicTrait extends CrudLogic[Comment, CommentPersistenceModel, CommentTable]