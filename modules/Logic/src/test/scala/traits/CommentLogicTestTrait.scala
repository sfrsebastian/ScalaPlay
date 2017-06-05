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
import org.mockito.Mockito.when
import slick.lifted.TableQuery
import tests.logic.CrudLogicTestTrait

trait CommentLogicTestTrait extends CrudLogicTestTrait[Comment, CommentPersistenceModel, CommentTable, CommentLogic, CommentPersistence]{

  var persistenceMock = mock[CommentPersistence]

  var logic = new CommentLogic(persistenceMock)

  when(persistenceMock.table) thenReturn mock[TableQuery[CommentTable]]

  override def generatePojo: Comment = factory.manufacturePojo(classOf[Comment])
}
