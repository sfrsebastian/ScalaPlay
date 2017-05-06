package auth.models.rule

import play.api.libs.json.Json

/**
  * Created by sfrsebastian on 4/22/17.
  */
case class Rule(val authentication:String, authorization:Option[List[String]], path:String, authorizationOp:Option[String], method:String)

object Rule{
  implicit val userJsonFormat = Json.format[Rule]
}
