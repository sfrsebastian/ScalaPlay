package common.auth.persistence

import java.sql.Timestamp
import java.util.UUID
import common.auth.models.{Token, Tokens}
import common.traits.layers.CrudPersistence
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Rep
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/15/17.
  */

trait TokenPersistenceTrait extends CrudPersistence[Token, Tokens]{
  override var table: TableQuery[Tokens] = Tokens.table

  val updateProjection: Tokens => (Rep[Timestamp], Rep[Boolean]) = b => (b.expiration, b.isSignUp)

  def updateTransform(element:Token): (Timestamp, Boolean) = (element.expirationTime, element.isSignUp)

  def update(id: Int, toUpdate: Token) : Future[Option[Token]] = {
    for {
      result <- db.run(table.filter(_.id === id).map(updateProjection).update(updateTransform(toUpdate)))
      updated <- get(id)
    } yield {
      result match{
        case 1 => updated
        case _ => None
      }
    }
  }

  def get(uuid:UUID):Future[Option[Token]] = get(table.filter(_.token === uuid))
}

class TokenPersistence extends TokenPersistenceTrait
