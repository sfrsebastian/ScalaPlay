import common.traits.layers.CrudPersistence
import common.traits.test.CrudPersistenceTestTrait
import models.bookModule.{Book, Books}
import persistence.bookModule.{BookPersistence, BookPersistenceTrait}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.inject.bind

class BookPersistenceTest extends CrudPersistenceTestTrait[Book, Books]{

  override var persistence: CrudPersistence[Book, Books] = new BookPersistence
  override var seedCollection: Seq[Book] = Nil
  override def generatePojo(): Book = factory.manufacturePojo(classOf[Book])

  override def assertByProperties(e1: Book, e2: Book): Unit = {
    super.assertByProperties(e1, e2)
    assert(e1.description == e2.description, "La descripcion deberia ser la misma")
    assert(e1.ISBN == e2.ISBN, "El ISBN deberia ser el mismo")
    assert(e1.image == e2.image, "La imagen deberia ser la misma")
  }
}