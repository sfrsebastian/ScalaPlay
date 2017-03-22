package persistence.bookModule

import models.bookModule._
import slick.lifted.TableQuery
import traits.CrudPersistence

object BookPersistence extends CrudPersistence[Book,Books] {
  override val table = TableQuery[Books]
}
