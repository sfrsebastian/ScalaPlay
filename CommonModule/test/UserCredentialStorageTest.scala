import java.util.UUID

import common.auth.models.Profile
import common.auth.persistence.entities.{Profiles, UserModel, Users}
import common.auth.persistence.managers.UserCredentialManager
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

  val credentialStore = new UserCredentialManager()
  val factory = new PodamFactoryImpl
  var user:UserModel = UserModel(1,UUID.randomUUID())
  var profiles:Seq[Profile] = Nil

  override def beforeAll(): Unit = {
    DatabaseOperations.createIfNotExist[Users](credentialStore.db, Users.table)
    DatabaseOperations.createIfNotExist[Profiles](credentialStore.db, Profiles.table)
  }

  override def beforeEach(){
    DatabaseOperations.Drop[Profiles](credentialStore.db, Profiles.table)
    DatabaseOperations.Drop[Users](credentialStore.db, Users.table)
    DatabaseOperations.createIfNotExist[Users](credentialStore.db, Users.table)
    DatabaseOperations.createIfNotExist[Profiles](credentialStore.db, Profiles.table)
    user = factory.manufacturePojo(classOf[UserModel])
    profiles = for {
      _ <- 0 to 3
    }yield factory.manufacturePojo(classOf[Profile]).copy(userId = 1)
    credentialStore.db.run(Users.table += user)
    credentialStore.db.run(Profiles.table ++= profiles)
  }

  override def afterAll():Unit = {
    //DatabaseOperations.Drop[Profile, Profiles](credentialStore.db, List(Profiles.table))
  }

  "UserDao" should {
    "save users and find them by userId" in {
      val profile = factory.manufacturePojo(classOf[Profile])
      whenReady(credentialStore.find(profiles.head.loginInfo)){result=>
        println("Recibido de test")
        println(result)
      }
    }
  }
}
