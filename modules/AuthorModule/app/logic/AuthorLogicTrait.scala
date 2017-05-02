package author.logic

import models.author._
import author.persistence.AuthorPersistenceTrait
import crud.layers.CrudLogic

/**
  * Created by sfrsebastian on 4/26/17.
  */
trait AuthorLogicTrait extends CrudLogic[Author, AuthorPersistenceModel, AuthorTable] {

  val persistence : AuthorPersistenceTrait

  override implicit def Model2Persistence = AuthorPersistenceConverter

  override implicit def Persistence2Model = PersistenceAuthorConverter
}
