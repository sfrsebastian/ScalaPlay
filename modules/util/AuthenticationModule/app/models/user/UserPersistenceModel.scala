/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package auth.models.user

import java.util.UUID
import crud.models.Row

/**
  * Modelo de persistencia de usuario
  * @param id
  * @param uuid
  * @param name
  * @param lastName
  * @param confirmed
  * @param email
  * @param loginProviderID
  * @param loginProviderKey
  * @param hasher
  * @param password
  * @param salt
  * @param roles
  */
case class UserPersistenceModel(
   id: Int,
   uuid:UUID,
   name: String,
   lastName: String,
   confirmed: Boolean,
   email:String,
   loginProviderID:String, //Login info tuple
   loginProviderKey:String, //Login info tuple
   hasher:String, //PasswordTuple
   password:String,//PasswordTuple
   salt:Option[String],//PasswordTuple
   roles:String
) extends Row
