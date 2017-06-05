/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package traits

import editorial.logic.EditorialLogic
import editorial.model.{Editorial, EditorialPersistenceModel, EditorialTable}
import editorial.persistence.EditorialPersistence
import org.mockito.Mockito.when
import slick.lifted.TableQuery
import tests.logic.CrudLogicTestTrait

trait EditorialLogicTestTrait extends CrudLogicTestTrait[Editorial, EditorialPersistenceModel, EditorialTable, EditorialLogic, EditorialPersistence]{

  var persistenceMock = mock[EditorialPersistence]

  var logic = new EditorialLogic(persistenceMock)

  when(persistenceMock.table) thenReturn mock[TableQuery[EditorialTable]]

  override def generatePojo: Editorial = factory.manufacturePojo(classOf[Editorial])
}
