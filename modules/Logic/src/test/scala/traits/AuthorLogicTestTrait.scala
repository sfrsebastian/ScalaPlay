/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package traits

import author.logic.AuthorLogic
import author.model._
import author.persistence.AuthorPersistence
import org.mockito.Mockito.when
import slick.lifted.TableQuery
import tests.logic.CrudLogicTestTrait

trait AuthorLogicTestTrait extends CrudLogicTestTrait[Author, AuthorPersistenceModel, AuthorTable, AuthorLogic, AuthorPersistence]{

  var persistenceMock = mock[AuthorPersistence]

  var logic = new AuthorLogic(persistenceMock)

  when(persistenceMock.table) thenReturn mock[TableQuery[AuthorTable]]

  override def generatePojo: Author = factory.manufacturePojo(classOf[Author])
}
