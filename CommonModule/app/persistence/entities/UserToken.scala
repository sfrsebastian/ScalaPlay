package common.auth.persistence.entities
import java.sql.{Date, Timestamp}
import java.util.{Calendar, UUID}
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

/**
  * Created by sfrsebastian on 4/15/17.
  */
case class UserToken(id:Int, token:UUID, email:String, expirationTime:Timestamp, isSignUp:Boolean, userId:Int) {
  def isExpired = expirationTime.before(Calendar.getInstance().getTime)
}

class UserTokens(tag:Tag) extends Table[UserToken](tag, "USER_TOKENS") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def token = column[UUID]("TOKEN")
  def email = column[String]("EMAIL")
  def expiration = column[Timestamp]("EXPIRATION")
  def isSignUp = column[Boolean]("ISSIGNUP")
  def userId = column[Int]("USERID")
  def userIdConstraint = foreignKey("TOKEN_USER_FK", userId, Users.table)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  def * = (id, token, email, expiration, isSignUp, userId) <> ((UserToken.apply _).tupled, UserToken.unapply _)
}

object UserToken {
  val table = TableQuery[UserTokens]
  def create(userId:Int, email:String, isSignUp:Boolean) ={
    Calendar.getInstance().add(Calendar.HOUR_OF_DAY, 12)
    UserToken(1, UUID.randomUUID(), email, new Timestamp(Calendar.getInstance().getTimeInMillis), isSignUp, userId)
  }

}