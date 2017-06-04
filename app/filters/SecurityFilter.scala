/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
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

/**
  * Inicializa el filtro de seguridad de la aplicación
  */
class SecurityFilter @Inject() (implicit val mat: Materializer, ec: ExecutionContext, override val silhouette:Silhouette[AuthenticationEnvironment], conf:Configuration) extends Filter with AuthenticationManager {

  /**
    * Convertidor implícito de Rule
    */
  private implicit val formatRule = Json.format[Rule]

  /**
    * Convertidor implícito de UserMin
    */
  private implicit val formatUser = Json.format[UserMin]

  /**
    * La configuración de reglas de seguridad en el archivo de configuración
    */
  private val json = conf.getList("security.rules").get.render(ConfigRenderOptions.concise())

  /**
    * Las reglas de configuración en formato Json
    */
  private val rules = Json.parse(json).as[List[Rule]]

  /**
    * @param nextFilter El siguiente filtro a llamar
    * @param requestHeader La información de la solicitud HTTP
    * @return
    */
  def apply(nextFilter: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    //Se valida que la solicitud no sea del controlador de autenticación.
    if(!requestHeader.path.contains("auth")){
      rules.find(rule => requestHeader.path.matches(rule.path) && rule.method == requestHeader.method) match {
        //Si alguna expresión de seguridad es acorde a la ruta, esta se aplica
        case Some(r) =>
          r.authentication match{
            //Si es de tipo secured se busca el operador
            case "Secured" =>
              r.authorizationOp.get match {
                //Si el operador es OR se aplica SecuredAction(WithRoles)
                case "OR" =>
                  SecuredAction(WithRoles(r.authorization.get.mkString(","))).async { implicit request =>
                    nextFilter(request.copy(tags = request.tags.+("identity" -> Json.toJson(request.identity.toMin).toString())))
                  }(requestHeader).run()
                //Si el operador es OR se aplica SecuredAction(WithRole)
                case "AND" =>
                  SecuredAction(WithRole(r.authorization.get.mkString(","))).async { implicit request =>
                    nextFilter(request.copy(tags = request.tags.+("identity" -> Json.toJson(request.identity.toMin).toString())))
                  }(requestHeader).run()
              }
            //Si es de tipo userAware se extrae el usuario de la petición
            case "UserAware" =>
              UserAwareAction.async{implicit request =>
                request.identity match {
                  //Si existe un usuario se agrega como un tag de la solicitud.
                  case Some(user) =>
                    nextFilter(request.copy(tags = request.tags. + ("identity" -> Json.toJson(user.toMin).toString())))
                  //No se aplica ninguna acción de seguridad
                  case None => nextFilter(requestHeader)
                }
              }(requestHeader).run()
            //No se aplica ninguna acción de seguridad
            case _ => nextFilter(requestHeader)
          }
        //No se aplica ninguna acción de seguridad
        case None => nextFilter(requestHeader)
      }
    }
    else{
      //No se aplica ninguna acción de seguridad
      nextFilter(requestHeader)
    }
  }
}