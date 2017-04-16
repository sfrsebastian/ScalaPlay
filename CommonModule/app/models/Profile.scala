package common.auth.models

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.providers.OAuth1Info
import common.traits.model.Row

/**
  * Created by sfrsebastian on 4/14/17.
  */
case class Profile(
  id:Int,
  name: String,
  lastName: Option[String],
  confirmed: Boolean,
  email:Option[String],
  fullName: Option[String],
  avatarUrl: Option[String],
  userId: Int,
  loginInfo:LoginInfo,
  passwordInfo:Option[PasswordInfo],
  oauth1Info: Option[OAuth1Info]
) extends Row
