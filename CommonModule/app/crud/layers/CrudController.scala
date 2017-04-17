package common.traits.layers

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.actions.{SecuredRequest, UserAwareRequest}
import com.mohiva.play.silhouette.api.{Environment, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import com.mohiva.play.silhouette.impl.providers.SocialProviderRegistry
import common.auth.models.User
import common.settings.auth.MyEnv
import common.traits.model.Entity
import play.Environment
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.{Format, Json}
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import com.mohiva.play.silhouette.api.{LoginEvent, LoginInfo, LogoutEvent, SignUpEvent, Silhouette}
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.{Clock, Credentials, PasswordHasherRegistry}
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import com.mohiva.play.silhouette.impl.exceptions.{IdentityNotFoundException, InvalidPasswordException}
import com.mohiva.play.silhouette.api.Authenticator.Implicits._
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/12/17.
  */
abstract class CrudController[T, K <: Entity[T]] extends Controller{

  val logic:CrudLogic[T, K]

  implicit val format:Format[T]

  def getAll = Action.async{
    logic.getAll.map(elements => Ok(Json.toJson(elements)))
  }

  def get(id:Int) = Action.async{
    logic.get(id).map(_ match {
      case None => BadRequest("El recurso no existe")
      case Some(elemento) => Ok(Json.toJson(elemento))
    })
  }

  def create() = Action.async(parse.json){ request =>
    request.body.validateOpt[T].getOrElse(None) match {
      case Some(x) => logic.create(x).map{
        case Some(element) => Created(Json.toJson(element))
        case None => BadRequest("El recurso no pudo ser creado")
      }
      case None => Future(BadRequest("Error en formato de contenido"))
    }
  }

  def update(id:Int) = Action.async(parse.json) { request =>
    request.body.validateOpt[T].getOrElse(None) match {
      case Some(x) => {
        logic.update(id, x).map(_ match{
          case Some(elemento) => Ok(Json.toJson(elemento))
          case None => BadRequest("El recurso no pudo ser actualizado")
        })
      }
      case None => Future(BadRequest("Error en formato de contenido"))
    }
  }

  def delete(id:Int) = Action.async{
    logic.delete(id).map(_ match{
      case Some(x) => {
        Ok(Json.toJson(x))
      }
      case None => BadRequest("El recurso con id dado no existe")
    })
  }
}
