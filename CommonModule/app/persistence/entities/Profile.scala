package common.auth.persistence.entities

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.providers.OAuth1Info
import common.auth.models.Profile
import common.traits.model.Entity
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

/**
  * Created by sfrsebastian on 4/14/17.
  */

object Profiles {
  val table = TableQuery[Profiles]
}

class Profiles(tag:Tag) extends Entity[Profile](tag, "PROFILES") {
  private type LoginInfoTuple = (String, String)
  private type PasswordInfoTuple = (String, String, Option[String])
  private type Oauth1Tuple = (String, String)
  private type ProfileTuple = (Int, String, Option[String], Boolean, Option[String], Option[String], Option[String], Int, LoginInfoTuple, PasswordInfoTuple, Oauth1Tuple)

  override def name = column[String]("FIRST_NAME")
  def lastName = column[Option[String]]("LAST_NAME")
  def confirmed = column[Boolean]("CONFIRMED")
  def email = column[Option[String]]("EMAIL")
  def fullName = column[Option[String]]("FULL_NAME")
  def avatarUrl = column[Option[String]]("AVATAR_URL")
  def loginProviderId = column[String]("PROVIDER_ID") //Login info
  def loginProviderKey = column[String]("PROVIDER_KEY") //Login info
  def hasher = column[String]("HASHER") //PasswordInfo
  def password = column[String]("PASSWORD") //PasswordInfo
  def salt = column[Option[String]]("SALT") //PasswordInfo
  def oauth1Secret = column[String]("OAUTH1_SECRET") //oauth1
  def oauth1Token = column[String]("OAUTH1_TOKEN") //oauth1
  def userId = column[Int]("USER_ID")
  def userIdConstraint = foreignKey("PROFILE_USER_FK", userId, Users.table)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)

  def * = profileShapedValue.shaped <> (toModel, toTuple)

  private val profileShapedValue = (
    id, name, lastName, confirmed, email,fullName, avatarUrl, userId,
    (loginProviderId, loginProviderKey),
    (hasher, password, salt),
    (oauth1Secret, oauth1Token)
  )

  private val toModel: ProfileTuple => Profile = {profile=>
    Profile(
      profile._1, profile._2, profile._3, profile._4, profile._5, profile._6, profile._7, profile._8,
      loginInfo = LoginInfo.tupled.apply(profile._9),
      passwordInfo = Some(PasswordInfo.tupled.apply(profile._10)),
      oauth1Info = Some(OAuth1Info.tupled.apply(profile._11))
    )
  }

  private val toTuple: Profile => Option[ProfileTuple] = { profile =>
    val passwordInfo:PasswordInfoTuple =  profile.passwordInfo match {
      case Some(value) => PasswordInfo.unapply(value).get
      case None => ("", "", None)
    }

    val oauthInfo:Oauth1Tuple = profile.oauth1Info match {
      case Some(value) => OAuth1Info.unapply(value).get
      case None => ("", "")
    }

    Some {
      (
        profile.id, profile.name, profile.lastName, profile.confirmed, profile.email, profile.fullName, profile.avatarUrl, profile.userId,
        (LoginInfo.unapply(profile.loginInfo).get),
        passwordInfo,
        oauthInfo
      )
    }
  }
}