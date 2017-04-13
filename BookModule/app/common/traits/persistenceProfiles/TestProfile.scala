package common.traits.persistenceProfiles

import slick.jdbc.PostgresProfile.api._
/**
  * Created by sfrsebastian on 4/10/17.
  */
trait TestProfile extends DatabaseProfile{
  def db = Database.forConfig("PostgresDBTest")
}
