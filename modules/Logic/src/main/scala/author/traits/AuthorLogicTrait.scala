package author.traits

import author.model._
import layers.logic.CrudLogic

/**
  * Created by sfrsebastian on 4/26/17.
  */
trait AuthorLogicTrait extends CrudLogic[Author, AuthorPersistenceModel, AuthorTable]
