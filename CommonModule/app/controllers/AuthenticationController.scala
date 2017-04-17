package controllers.common.auth

import java.util.UUID
import com.google.inject.Inject
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.{Environment, LoginInfo, Silhouette}
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.{Clock, Credentials, PasswordHasher, PasswordInfo}
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import common.auth.logic.{AuthLogic, Mailer}
import common.auth.models._
import common.settings.auth.MyEnv
import common.traits.layers.AuthController
import common.auth.models.{SignInForm, SignUpForm}
import play.api.Configuration
import play.api.i18n.MessagesApi
import play.api.libs.json.{Format, Json}
import play.api.mvc.{Action, Controller}
import net.ceedubs.ficus.Ficus._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import common.auth.logic.Implicits.SumDateTime
/**
  * Created by sfrsebastian on 4/15/17.
  */
object AuthenticationController
class AuthenticationController @Inject() (
                                           override val silhouette: Silhouette[MyEnv],
                                           override val messagesApi: MessagesApi,
                                           env:Environment[MyEnv],
                                           authInfoRepository: AuthInfoRepository,
                                           credentialsProvider: CredentialsProvider,
                                           authLogic: AuthLogic,
                                           passwordHasher: PasswordHasher,
                                           configuration: Configuration,
                                           clock:Clock,
                                           mailer: Mailer) extends Controller with AuthController{

  implicit val signUpFormat:Format[SignUpForm] = Json.format[SignUpForm]
  implicit val signInFormat:Format[SignInForm] = Json.format[SignInForm]

  def signUp(redirect:String) = Action.async(parse.json){implicit request =>
    request.body.validateOpt[SignUpForm].getOrElse(None) match {
      case Some(form) => {
        val loginInfo = LoginInfo(CredentialsProvider.ID, form.email)
        authLogic.retrieve(loginInfo).flatMap{
          case Some(_) => Future(BadRequest("El correo dado ya tiene un usuario asignado"))
          case None => {
            for {
              user <- authLogic.create(User.generateUser(form, loginInfo, PasswordInfo("", "", None)))
              _ <- authInfoRepository.add(loginInfo, passwordHasher.hash(form.password))
              token <- authLogic.create(Token.create(user.id, form.email, true))
            } yield {
              mailer.welcome(user.fullName, user.email,link = routes.AuthenticationController.confirmAccount(token.id.toString, redirect.toString).absoluteURL())
              Ok("Usuario creado correctamente")
            }
          }
        }
      }
      case None => Future(BadRequest("Error en formato de formulario de creacion"))
    }
  }

  def confirmAccount(tokenId:String, redirect:String) = Action.async { implicit request =>
    val tokenUUID = UUID.fromString(tokenId)
    authLogic.getToken(tokenUUID).flatMap {
      case None => Future(BadRequest("No se encontró usuario asociado"))
      case Some(token) if token.isSignUp && !token.isExpired =>
        authLogic.getUser(token.userId).flatMap {
          case None => Future(InternalServerError("Error validando cuenta, contacta al administrador"))
          case Some(user) =>
            val loginInfo = LoginInfo(CredentialsProvider.ID, user.email)
            for {
              authenticator <- env.authenticatorService.create(loginInfo)
              value <- env.authenticatorService.init(authenticator)
              _ <- authLogic.confirm(loginInfo)
              _ <- authLogic.deleteToken(token.id)
              result <- env.authenticatorService.embed(value, Redirect(redirect.toString))
            } yield result
        }
      case Some(token) =>
        authLogic.deleteToken(token.id)
        Future(Ok("Enlace vencido, debes solicitar un nuevo link para validar tu cuenta"))
    }
  }

  def signIn(redirect:String) = Action.async(parse.json) { implicit request =>
    request.body.validateOpt[SignInForm].getOrElse(None) match {
      case None => Future(BadRequest("Error en formato de formulario"))
      case Some(form) => {
        val credentials = Credentials(form.email, form.password)
        credentialsProvider.authenticate(credentials).flatMap { loginInfo =>
          authLogic.retrieve(loginInfo).flatMap {
            case None => Future(InternalServerError("Error autenticando usuario, contacta al administrador"))
            case Some(user) if !user.confirmed => Future(BadRequest("El usuario debe confirmar su cuenta"))
            case Some(_) => for {
              authenticator <- env.authenticatorService.create(loginInfo).map(authenticatorWithRememberMe(_, form.rememberMe))
              value <- env.authenticatorService.init(authenticator)
              result <- env.authenticatorService.embed(value, Redirect(redirect))
            } yield result
          }
        }.recover {
          case _:ProviderException => BadRequest("Usuario y/o contraseña invalidos")
        }
      }
    }
  }

  def signOut(redirect:String) = SecuredAction.async { implicit request =>
    env.authenticatorService.discard(request.authenticator, Redirect(redirect))
  }

  private def authenticatorWithRememberMe(authenticator: CookieAuthenticator, rememberMe: Boolean) = {
    if (rememberMe) {
      authenticator.copy(
        expirationDateTime = clock.now + rememberMeParams._1,
        idleTimeout = rememberMeParams._2,
        cookieMaxAge = rememberMeParams._3
      )
    }
    else
      authenticator
  }
  private lazy val rememberMeParams: (FiniteDuration, Option[FiniteDuration], Option[FiniteDuration]) = {
    val cfg = configuration.getConfig("silhouette.authenticator.rememberMe").get.underlying
    (
      cfg.as[FiniteDuration]("authenticatorExpiry"),
      cfg.getAs[FiniteDuration]("authenticatorIdleTimeout"),
      cfg.getAs[FiniteDuration]("cookieMaxAge")
    )
  }

 /* def authenticate = Action.async { implicit request =>
    AuthForms.signInForm.bindFromRequest.fold(
      bogusForm => Future.successful(BadRequest(views.html.auth.signIn(bogusForm))),
      signInData => {
        val credentials = Credentials(signInData.email, signInData.password)
        credentialsProvider.authenticate(credentials).flatMap { loginInfo =>
          userLogic.retrieve(loginInfo).flatMap {
            case None =>
              Future.successful(Redirect(routes.AuthenticationController.signIn()).flashing("error" -> Messages("error.noUser")))
            case Some(user) if !user.profileFor(loginInfo).map(_.confirmed).getOrElse(false) =>
              Future.successful(Redirect(routes.AuthenticationController.signIn).flashing("error" -> Messages("error.unregistered", signInData.email)))
            case Some(_) => for {
              authenticator <- env.authenticatorService.create(loginInfo).map {
                case authenticator if signInData.rememberMe =>
                  val c = configuration.underlying
                  authenticator.copy(
                    expirationDateTime = new DateTime(),//+ c.as[FiniteDuration]("silhouette.authenticator.rememberMe.authenticatorExpiry"),
                    idleTimeout = c.getAs[FiniteDuration]("silhouette.authenticator.rememberMe.authenticatorIdleTimeout"),
                    cookieMaxAge = c.getAs[FiniteDuration]("silhouette.authenticator.rememberMe.cookieMaxAge")
                  )
                case authenticator => authenticator
              }
              value <- env.authenticatorService.init(authenticator)
              result <- env.authenticatorService.embed(value, Redirect(routes.AuthenticationController.index()))
            } yield result
          }
        }.recover {
          case e:ProviderException => Redirect(routes.AuthenticationController.signIn()).flashing("error" -> Messages("error.invalidCredentials"))
        }
      }
    )
  }*/

  def index = UserAwareAction.async { implicit request =>
    request.identity match{
      case Some(user) => Future(Ok(Json.toJson(user)))
      case None => Future(Ok("No hay usuario autenticado"))
    }
  }

  def profile = SecuredAction { implicit request =>
    Ok("Segura")
  }
}
