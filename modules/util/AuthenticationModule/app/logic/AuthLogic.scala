/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package auth.logic

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import auth.models.user.User
import auth.persistence.UserPersistenceTrait
import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

/**
  * La logica de manejo de usuarios
  * @param userPersistence La persistencia del usuario
  */
class AuthLogic @Inject() (userPersistence: UserPersistenceTrait) extends IdentityService[User] {

  /**
    * Retorna un usuario a partir de su información de login
    * @param loginInfo La información de login
    */
  def retrieve(loginInfo:LoginInfo) = userPersistence.runAction(userPersistence.getAction(userPersistence.table.filter(u => u.loginProviderId === loginInfo.providerID && u.loginProviderKey === u.loginProviderKey)))

  /**
    * Crea un usuario en la base datos
    * @param user el usuario a crear
    */
  def create(user:User) : Future[User] = userPersistence.runAction(userPersistence.createAction(user))

  /**
    * Retorna un usuario dado su id.
    * @param id el id del usurio
    */
  def getUser(id:Int) : Future[Option[User]] = userPersistence.runAction(userPersistence.getAction(userPersistence.table.filter(_.id === id)))

  /**
    * Confirma el email del usuario con información de login dado.
    * @param loginInfo La información de login
    */
  def confirm(loginInfo:LoginInfo) : Future[Option[User]] = userPersistence.runAction(userPersistence.confirm(loginInfo))
}