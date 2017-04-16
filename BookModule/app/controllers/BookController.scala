package controllers.bookModule

import javax.inject.Inject

import com.mohiva.play.silhouette.api.{Environment, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import com.mohiva.play.silhouette.impl.providers.SocialProviderRegistry
import common.auth.models.User
import common.settings.auth.MyEnv
import common.traits.layers.{AuthController, CrudController}
import logic.bookModule.BookLogicTrait
import models.bookModule.{Book, Books}
import play.api.i18n.MessagesApi
import play.api.libs.json.Json

class BookController @Inject()(override val logic:BookLogicTrait, override val silhouette: Silhouette[MyEnv], val messagesApi: MessagesApi) extends CrudController[Book, Books] {
  override implicit val format = Json.format[Book]
}