package auth.models.user

import java.util.UUID
import crud.models.Row

/**
  * Created by sfrsebastian on 5/5/17.
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
