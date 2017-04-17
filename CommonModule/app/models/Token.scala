package common.auth.models

import java.sql.Timestamp
import java.util.{Calendar, UUID}
import common.traits.model.Entity
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

/**
  * Created by sfrsebastian on 4/16/17.
  */

case class Token(id:Int, token:UUID, email:String, expirationTime:Timestamp, isSignUp:Boolean, userId:Int) {
  def isExpired = expirationTime.before(Calendar.getInstance().getTime)
}

object Token{
  def create(userId:Int, email:String, isSignUp:Boolean) ={
    val calendar = Calendar.getInstance
    calendar.add(Calendar.HOUR_OF_DAY, 12)
    val milis = calendar.getTimeInMillis
    Token(1, UUID.randomUUID(), email, new Timestamp(milis), isSignUp, userId)
  }
}

class Tokens(tag:Tag) extends Entity[Token](tag, "TOKENS") {
  def token = column[UUID]("TOKEN")
  override def name = column[String]("EMAIL")
  def expiration = column[Timestamp]("EXPIRATION")
  def isSignUp = column[Boolean]("ISSIGNUP")
  def userId = column[Int]("USERID")
  def userIdConstraint = foreignKey("TOKEN_USER_FK", userId, Users.table)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  def * = (id, token, name, expiration, isSignUp, userId) <> ((Token.apply _).tupled, Token.unapply _)
}

object Tokens {
  val table = TableQuery[Tokens]
}
