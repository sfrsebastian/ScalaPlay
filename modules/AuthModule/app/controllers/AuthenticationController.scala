package controllers.auth

import java.util.UUID

import auth.controllers.AuthController
import com.google.inject.Inject
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.{Environment, LoginInfo, Silhouette}
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.{Clock, Credentials, PasswordHasher, PasswordInfo}
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import auth.logic.{AuthLogic, Mailer}
import auth.models._
import auth.settings.MyEnv
import auth.models.{ResetPasswordFormEmail, ResetPasswordFormPassword, SignInForm, SignUpForm}
import play.api.Configuration
import play.api.libs.json.{Format, Json}
import play.api.mvc.{Action, Controller}
import net.ceedubs.ficus.Ficus._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import auth.logic.Implicits.SumDateTime

/**
  * Created by sfrsebastian on 4/15/17.
  */

object AuthenticationController
class AuthenticationController @Inject() (
                                           override val silhouette: Silhouette[MyEnv],
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
  implicit val passwordResetPFormat:Format[ResetPasswordFormPassword] = Json.format[ResetPasswordFormPassword]
  implicit val passwordResetEFormat:Format[ResetPasswordFormEmail] = Json.format[ResetPasswordFormEmail]

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

  def sendResetPassword(redirect:String) = Action.async(parse.json) { implicit request =>
    request.body.validateOpt[ResetPasswordFormEmail].getOrElse(None) match {
      case None => Future(BadRequest("Error en formato de formulario"))
      case Some(form) => authLogic.retrieve(LoginInfo(CredentialsProvider.ID, form.email)).flatMap {
        case None => Future(NotFound("Error validando solicitud, contacta al administrador"))
        case Some(user) => for {
          token <- authLogic.create(Token.create(user.id, user.email, isSignUp = false))
        } yield {
          mailer.resetPassword(user.email, link = redirect + "?tokenId=" + token.token.toString)
          Ok("Las instrucciones de cambio de contraseña han sido enviadas al correo del usuario")
        }
      }
    }
  }

  def resetPassword(tokenId:String) = Action.async(parse.json){implicit request =>
    request.body.validateOpt[ResetPasswordFormPassword].getOrElse(None) match {
      case None => Future(BadRequest("Error en formato de formulario"))
      case Some(form) => {
        val tokenUUID = UUID.fromString(tokenId)
        authLogic.getToken(tokenUUID).flatMap {
          case None => Future(NotFound("Error validando solicitud, contactar al administrador"))
          case Some(token) if !token.isSignUp && !token.isExpired => {
            val loginInfo = LoginInfo(CredentialsProvider.ID, token.email)
            for {
              _ <- authInfoRepository.save(loginInfo, passwordHasher.hash(form.password))
              authenticator <- env.authenticatorService.create(loginInfo)
              value <- env.authenticatorService.init(authenticator)
              _ <- authLogic.deleteToken(token.id)
              result <- env.authenticatorService.embed(value, Ok("Cambio de contraseña exitoso"))
            } yield result
          }
          case Some(token) => for {
            _ <- authLogic.deleteToken(token.id)
          } yield NotFound("Solicitud de cambio de contraseña vencida, debes realizar una nueva solicitud")
        }
      }
    }
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

  def index = UserAwareAction.async { implicit request =>
    request.identity match{
      case Some(user) => Future(Ok(Json.toJson(user.toMin)))
      case None => Future(Ok("No hay usuario autenticado"))
    }
  }

  def profile = SecuredAction { implicit request =>
    Ok("Segura")
  }
}
