package common.auth.persistence.managers

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import common.auth.models.{Profile, User}
import common.auth.persistence.entities.{Profiles, UserModel, Users}
import common.auth.persistence.traits.UserCredentialManagerTrait
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/14/17.
  */
class UserCredentialManager extends UserCredentialManagerTrait {
  def db:Database = Database.forConfig("Database")

  //Verificar
  def find(loginInfo:LoginInfo):Future[Option[User]] = {
    val st1 = for {
      profile <- Profiles.table.filter(p => p.loginProviderId === loginInfo.providerID && p.loginProviderKey === loginInfo.providerKey).take(1)
    } yield profile
    db.run(st1.result.headOption).flatMap {
      case Some(p) => find(p.userId)
      case None => Future(None)
    }
  }

  //Verificar
  def find(uuid:UUID):Future[Option[User]] = {
    find(Users.table.filter(_.uuid === uuid))
  }

  def find(userId:Int):Future[Option[User]] = {
    find(Users.table.filter(_.id === userId))
  }

  def find(query:Query[Users, UserModel, Seq]): Future[Option[User]] ={
    val join = for {
      (dbUsers, dbProfiles) <- query joinLeft Profiles.table on (_.id === _.userId)
    } yield (dbUsers, dbProfiles)
    db.run(join.result).map{results =>
      val profiles = results.map(_._2).flatten.toList
      results.headOption match {
        case Some((user, _)) => Some(User(user.id, user.uuid, profiles))
        case None => None
      }
    }
  }

  def save(user:User):Future[User] = {
    db.run(Users.table += UserModel(1,user.uuid))
    db.run(Profiles.table ++= user.profiles)
    find(user.uuid) map (_.get)
  }

  def confirm(loginInfo:LoginInfo):Future[User] = {
    val profile = for {
      p <- Profiles.table
      if p.loginProviderId === loginInfo.providerID && p.loginProviderKey === loginInfo.providerKey
    }yield(p.confirmed)
    db.run(profile.update(true))
    find(loginInfo) map (_.get)
  }

  def link(profile:Profile):Future[User] = {
    db.run(Profiles.table += profile)
    find(profile.userId) map (_.get)
  }

  def update(profile:Profile) = {
    val update = for {
      p <- Profiles.table
      if p.loginProviderId === profile.loginInfo.providerID && p.loginProviderKey === profile.loginInfo.providerKey
    }yield(p)
    db.run(update.update(profile))
    find(profile.userId) map (_.get)
  }
}
