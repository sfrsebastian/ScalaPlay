import common.auth.models.{User, Users}
import common.auth.persistence.UserPersistence
import common.utilities.DatabaseOperations
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatestplus.play.PlaySpec
import uk.co.jemos.podam.api.PodamFactoryImpl
import slick.jdbc.PostgresProfile.api._

/**
  * Created by sfrsebastian on 4/14/17.
  */
class UserCredentialStorageTest extends PlaySpec with BeforeAndAfterAll with BeforeAndAfterEach with ScalaFutures{
  private implicit val defaultPatience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  val credentialStore = new UserPersistence()
  val factory = new PodamFactoryImpl
  var user:User = factory.manufacturePojo(classOf[User])

  override def beforeAll(): Unit = {
    DatabaseOperations.createIfNotExist[User, Users](credentialStore.db, Users.table)
  }

  override def beforeEach(){
    DatabaseOperations.Drop[User, Users](credentialStore.db, Users.table)
    DatabaseOperations.createIfNotExist[User, Users](credentialStore.db, Users.table)
    user = factory.manufacturePojo(classOf[User])
    credentialStore.db.run(Users.table += user)
  }

  override def afterAll():Unit = {
    //DatabaseOperations.Drop[Profile, Profiles](credentialStore.db, List(Profiles.table))
  }

  "UserDao" should {
    "save users and find them by userId" in {

    }
  }
}
