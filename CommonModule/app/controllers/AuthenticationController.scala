package controllers.common.auth

import java.util.UUID

import views.html._
import com.google.inject.Inject
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.{LoginInfo, Silhouette}
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.services.AvatarService
import com.mohiva.play.silhouette.api.util.{Credentials, PasswordHasher}
import com.mohiva.play.silhouette.impl.exceptions.IdentityNotFoundException
import com.mohiva.play.silhouette.impl.providers.{CommonSocialProfileBuilder, CredentialsProvider, SocialProvider, SocialProviderRegistry}
import common.auth.logic.{Mailer, UserLogic, UserTokenLogic}
import common.auth.models._
import common.auth.persistence.entities.UserToken
import common.settings.auth.MyEnv
import common.traits.layers.AuthController
import controllers.AuthForms
import play.api.Configuration
import play.api.i18n.{Messages, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import net.ceedubs.ficus.Ficus._
import org.joda.time.DateTime
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/15/17.
  */
object AuthenticationController
class AuthenticationController @Inject() (
                                           override val silhouette: Silhouette[MyEnv],
                                           override val messagesApi: MessagesApi,
                                           authInfoRepository: AuthInfoRepository,
                                           credentialsProvider: CredentialsProvider,
                                           userLogic: UserLogic,
                                           userTokenLogic: UserTokenLogic,
                                           passwordHasher: PasswordHasher,
                                           configuration: Configuration,
                                           mailer: Mailer) extends Controller with AuthController{

  def startSignUp = UserAwareAction.async { implicit request =>
    Future.successful(request.identity match {
      case Some(user) => Redirect(routes.AuthenticationController.index)
      case None => Ok(views.html.auth.startSignUp(AuthForms.signUpForm))
    })
  }

  def handleStartSignUp = Action.async { implicit request =>
    AuthForms.signUpForm.bindFromRequest.fold(
      bogusForm => Future.successful(BadRequest(views.html.auth.startSignUp(bogusForm))),
      signUpData => {
        val loginInfo = LoginInfo(CredentialsProvider.ID, signUpData.email)
        userLogic.retrieve(loginInfo).flatMap {
          case Some(_) =>
            Future.successful(Redirect(routes.AuthenticationController.startSignUp()).flashing(
              "error" -> Messages("error.userExists", signUpData.email)))
          case None =>
            val profile = Profile(
              id = 1,
              name = signUpData.firstName,
              lastName = Some(signUpData.lastName),
              email = Some(signUpData.email),
              fullName = Some(s"${signUpData.firstName} ${signUpData.lastName}"),
              avatarUrl = None,
              userId = 1,
              confirmed = false,
              loginInfo = loginInfo,
              passwordInfo = None,
              oauth1Info = None)

            for {
              user <- userLogic.save(User(id = 1, uuid=UUID.randomUUID(), profiles = List(profile.copy(avatarUrl = None))))
              _ <- authInfoRepository.add(loginInfo, passwordHasher.hash(signUpData.password))
              token <- userTokenLogic.save(UserToken.create(user.id, signUpData.email, true))
            } yield {
              mailer.welcome(profile, link = routes.AuthenticationController.signUp(token.id.toString).absoluteURL())
              Ok(views.html.auth.finishSignUp())//profile
            }
        }
      }
    )
  }

  def signUp(tokenId:String) = Action.async { implicit request =>
    println("Sign up")
    Future(Ok("Ok"))
    val id = UUID.fromString(tokenId)
    userTokenLogic.find(id).flatMap {
      case None => Future.successful(NotFound(views.html.errors.notFound(request)))
      case Some(token) if token.isSignUp && !token.isExpired =>
        userLogic.find(token.token).flatMap {
          case None => Future.failed(new IdentityNotFoundException(Messages("error.noUser")))
          case Some(user) =>
            val loginInfo = LoginInfo(CredentialsProvider.ID, token.email)
            for {
              authenticator <- env.authenticatorService.create(loginInfo)
              value <- env.authenticatorService.init(authenticator)
              _ <- userLogic.confirm(loginInfo)
              _ <- userTokenLogic.remove(id)
              result <- env.authenticatorService.embed(value, Redirect(routes.AuthenticationController.index()))
            } yield result
        }
      case Some(token) =>
        userTokenLogic.remove(id).map {_ => NotFound(views.html.errors.notFound(request))}
    }
  }

  def signIn = UserAwareAction.async { implicit request =>
    Future.successful(request.identity match {
      case Some(user) => Redirect(routes.AuthenticationController.index())
      case None => Ok(views.html.auth.signIn(AuthForms.signInForm))
    })
  }

  def authenticate = Action.async { implicit request =>
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
  }

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
