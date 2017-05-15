package editorial.logic
import slick.jdbc.PostgresProfile.api._
import crud.layers.CrudLogic
import editorial.model.{Editorial, EditorialPersistenceModel, EditorialTable}
import editorial.persistence.EditorialPersistenceTrait

/**
  * Created by sfrsebastian on 4/26/17.
  */
trait EditorialLogicTrait extends CrudLogic[Editorial, EditorialPersistenceModel, EditorialTable] {
  val persistence : EditorialPersistenceTrait
}
