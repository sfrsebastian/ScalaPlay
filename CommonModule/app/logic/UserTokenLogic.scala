package common.auth.logic

import java.util.UUID

import com.google.inject.Inject
import common.auth.persistence.entities.UserToken
import common.auth.persistence.managers.traits.TokenManagerTrait

/**
  * Created by sfrsebastian on 4/15/17.
  */
class UserTokenLogic @Inject() (tokenManager:TokenManagerTrait) {
  def find(token:UUID) = tokenManager.find(token)
  def save(token:UserToken) = tokenManager.save(token)
  def remove(token:UUID) = tokenManager.remove(token)
}