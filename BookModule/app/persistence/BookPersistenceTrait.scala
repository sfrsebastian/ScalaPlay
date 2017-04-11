package persistence.bookModule

import models.bookModule.{Book, Books}
import slick.lifted.TableQuery
import traits.CrudPersistence

/**
  * Created by sfrsebastian on 4/10/17.
  */
trait BookPersistenceTrait extends CrudPersistence[Book, Books] {
  val table = TableQuery[Books]
}
