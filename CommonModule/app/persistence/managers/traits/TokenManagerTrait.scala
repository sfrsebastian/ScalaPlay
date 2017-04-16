package common.auth.persistence.managers.traits

import java.util.UUID

import common.auth.persistence.entities.UserToken

import scala.concurrent.Future

/**
  * Created by sfrsebastian on 4/15/17.
  */
trait TokenManagerTrait {
  def find(id:UUID):Future[Option[UserToken]]
  def save(token:UserToken):Future[UserToken]
  def remove(id:UUID):Future[Option[UserToken]]
}
