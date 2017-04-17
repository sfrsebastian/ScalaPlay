import book.models.{Book, Books}
import book.persistence.BookPersistence
import crud.layers.CrudPersistence
import crud.tests.CrudPersistenceTestTrait

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