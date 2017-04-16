package common.auth.persistence.managers

import java.util.UUID

import common.auth.persistence.entities.UserToken
import common.auth.persistence.managers.traits.TokenManagerTrait
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/15/17.
  */
class TokenManager extends TokenManagerTrait{

  def db:Database = Database.forConfig("Database")

  def find(token:UUID):Future[Option[UserToken]] = {
    db.run(UserToken.table.filter(_.token === token).result.headOption)
  }

  def save(token:UserToken):Future[UserToken] = {
    db.run(UserToken.table += token)
    find(token.token).map(_.get)
  }

  def remove(token:UUID):Future[Option[UserToken]] = {
    for {
      toDelete <- find(token)
      i <- db.run(UserToken.table.filter(_.token === token).delete)
      if(i == 1)
    }yield toDelete
  }
}
