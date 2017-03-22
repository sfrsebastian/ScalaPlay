package persistence.bookModule

import models.bookModule._
import slick.lifted.TableQuery
import traits.CrudPersistence

/**
  * Created by sfrsebastian on 3/21/17.
  */
object BookPersistence extends CrudPersistence[Book,Books] {
  override val table = TableQuery[Books]
}
