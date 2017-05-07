package filters

import akka.stream.Materializer
import auth.controllers.AuthenticationManager
import auth.models.role.{WithRole, WithRoles}
import auth.models.rule.Rule
import auth.models.user.UserMin
import auth.settings.AuthenticationEnvironment
import com.google.inject.Inject
import com.mohiva.play.silhouette.api.Silhouette
import com.typesafe.config.ConfigRenderOptions
import play.api.Configuration
import play.api.libs.json.Json
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}

class SecurityFilter @Inject() (implicit val mat: Materializer, ec: ExecutionContext, override val silhouette:Silhouette[AuthenticationEnvironment], conf:Configuration) extends Filter with AuthenticationManager {

  implicit val formatRule = Json.format[Rule]

  implicit val formatUser = Json.format[UserMin]

  val json = conf.getList("security.rules").get.render(ConfigRenderOptions.concise())
  val rules = Json.parse(json).as[List[Rule]]


  def apply(nextFilter: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    if(!requestHeader.path.contains("auth")){
      rules.find(rule => requestHeader.path.matches(rule.path) && rule.method == requestHeader.method) match {
        case Some(r) => {
          r.authentication match{
            case "Secured" => {
              r.authorizationOp.get match {
                case "OR" => {
                  SecuredAction(WithRoles(r.authorization.get.mkString(","))).async { implicit request =>
                    nextFilter(request.copy(tags = request.tags.+("identity" -> Json.toJson(request.identity.toMin).toString())))
                  }(requestHeader).run()
                }
                case "AND" => {
                  SecuredAction(WithRole(r.authorization.get.mkString(","))).async { implicit request =>
                    nextFilter(request.copy(tags = request.tags.+("identity" -> Json.toJson(request.identity.toMin).toString())))
                  }(requestHeader).run()
                }
              }
            }
            case "UserAware" => {
              UserAwareAction.async{implicit request =>
                request.identity match {
                  case Some(user) => {
                    nextFilter(request.copy(tags = request.tags. + ("identity" -> Json.toJson(user.toMin).toString())))
                  }
                  case None => nextFilter(requestHeader)
                }
              }(requestHeader).run()
            }
            case _ => nextFilter(requestHeader)
          }
        }
        case None => nextFilter(requestHeader)
      }
    }
    else{
      nextFilter(requestHeader)
    }
  }
}