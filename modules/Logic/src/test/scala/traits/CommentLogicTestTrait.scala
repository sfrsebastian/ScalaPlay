package traits

import comment.logic.CommentLogic
import comment.model._
import comment.persistence.CommentPersistence
import org.mockito.Mockito._
import slick.jdbc.PostgresProfile.api._
import tests.logic.CrudLogicTestTrait

/**
  * Created by sfrsebastian on 4/12/17.
  */
trait CommentLogicTestTrait extends CrudLogicTestTrait[Comment, CommentPersistenceModel, CommentTable, CommentLogic, CommentPersistence]{

  var persistenceMock = mock[CommentPersistence]

  var logic = new CommentLogic(persistenceMock)

  override def beforeEach(){
    persistenceMock = mock[CommentPersistence]
    when(persistenceMock.table) thenReturn mock[TableQuery[CommentTable]]
    logic = new CommentLogic(persistenceMock)
  }

  override def generatePojo: Comment = factory.manufacturePojo(classOf[Comment])
}
