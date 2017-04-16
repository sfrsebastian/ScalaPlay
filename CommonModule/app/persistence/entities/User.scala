package common.auth.persistence.entities

import java.util.UUID

import common.auth.models.{Profile, User}
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

/**
  * Created by sfrsebastian on 4/14/17.
  */

case class UserModel(id:Int, uuid:UUID)

object Users{
  val table = TableQuery[Users]
  def toUser(model:UserModel, profiles:List[Profile]):User = {
    User(model.id, model.uuid, profiles)
  }
}
class Users(tag:Tag) extends Table[UserModel](tag, "USERS") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def uuid = column[UUID]("UUID")
  def * = (id, uuid) <> (UserModel.tupled, UserModel.unapply _)
}

