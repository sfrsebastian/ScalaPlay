package traits.profiles

import slick.jdbc.PostgresProfile.api._
/**
  * Created by sfrsebastian on 4/10/17.
  */
trait DevProfile extends DatabaseProfile{
  def db = Database.forConfig("PostgresDB")
}
