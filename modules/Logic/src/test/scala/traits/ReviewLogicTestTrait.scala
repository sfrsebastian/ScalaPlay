/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package traits

import review.logic.ReviewLogic
import review.model._
import review.persistence.ReviewPersistence
import org.mockito.Mockito.when
import slick.lifted.TableQuery
import tests.logic.CrudLogicTestTrait

trait ReviewLogicTestTrait extends CrudLogicTestTrait[Review, ReviewPersistenceModel, ReviewTable, ReviewLogic, ReviewPersistence]{

  var persistenceMock = mock[ReviewPersistence]

  var logic = new ReviewLogic(persistenceMock)

  when(persistenceMock.table) thenReturn mock[TableQuery[ReviewTable]]

  override def generatePojo: Review = factory.manufacturePojo(classOf[Review])
}
