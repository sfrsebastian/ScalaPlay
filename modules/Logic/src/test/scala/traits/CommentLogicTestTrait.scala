/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package traits

import comment.logic.CommentLogic
import comment.model._
import comment.persistence.CommentPersistence
import tests.logic.CrudLogicTestTrait

trait CommentLogicTestTrait extends CrudLogicTestTrait[Comment, CommentPersistenceModel, CommentTable, CommentLogic, CommentPersistence]{

  var persistenceMock = mock[CommentPersistence]

  var logic = new CommentLogic(persistenceMock)

  override def generatePojo: Comment = factory.manufacturePojo(classOf[Comment])
}
