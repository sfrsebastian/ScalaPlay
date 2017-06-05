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
import tests.logic.CrudLogicTestTrait

trait AuthorLogicTestTrait extends CrudLogicTestTrait[Author, AuthorPersistenceModel, AuthorTable, AuthorLogic, AuthorPersistence]{

  var persistenceMock = mock[AuthorPersistence]

  var logic = new AuthorLogic(persistenceMock)

  override def generatePojo: Author = factory.manufacturePojo(classOf[Author])
}
