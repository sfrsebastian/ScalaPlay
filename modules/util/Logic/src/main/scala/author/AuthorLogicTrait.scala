package author.logic

import author.persistence.AuthorPersistenceTrait
import crud.layers.CrudLogic
import author.model._

/**
  * Created by sfrsebastian on 4/26/17.
  */
trait AuthorLogicTrait extends CrudLogic[Author, AuthorPersistenceModel, AuthorTable] {

  val persistence : AuthorPersistenceTrait

  override implicit def Model2Persistence = AuthorPersistenceConverter

  override implicit def Persistence2Model = PersistenceAuthorConverter
}
