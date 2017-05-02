import book.persistence.BookPersistence
import crud.layers.CrudPersistence
import crud.tests.CrudPersistenceTestTrait
import models.book.{BookPersistenceModel, BookTable}

class BookPersistenceTest extends CrudPersistenceTestTrait[BookPersistenceModel, BookTable]{

  override var persistence: CrudPersistence[BookPersistenceModel, BookTable] = new BookPersistence
  override var seedCollection: Seq[BookPersistenceModel] = Nil
  override def generatePojo(): BookPersistenceModel = factory.manufacturePojo(classOf[BookPersistenceModel])

  override def assertByProperties(e1: BookPersistenceModel, e2: BookPersistenceModel): Unit = {
    super.assertByProperties(e1, e2)
    assert(e1.description == e2.description, "La descripcion deberia ser la misma")
    assert(e1.ISBN == e2.ISBN, "El ISBN deberia ser el mismo")
    assert(e1.image == e2.image, "La imagen deberia ser la misma")
  }
}