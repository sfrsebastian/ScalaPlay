import comment.logic.CommentLogic
import comment.models.{Comment, Comments}
import comment.persistence.CommentPersistence
import crud.tests.CrudLogicTestTrait
import org.mockito.Mockito._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/12/17.
  */
class CommentLogicTest extends CrudLogicTestTrait[Comment, Comments, CommentLogic, CommentPersistence]{

  var persistenceMock = mock[CommentPersistence]
  var logic = new CommentLogic(persistenceMock)

  override def beforeEach(){
    persistenceMock = mock[CommentPersistence]
    when(persistenceMock.table) thenReturn mock[TableQuery[Comments]]
    logic = new CommentLogic(persistenceMock)
  }

  override def generatePojo: Comment = factory.manufacturePojo(classOf[Comment])
}
