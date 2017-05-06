package editorial.model

import book.model.{Book, BookMinConverter}
import crud.models.ModelConverter

/**
  * Created by sfrsebastian on 5/1/17.
  */
object EditorialPersistenceConverter extends ModelConverter[Editorial, EditorialPersistenceModel] {
  override def convert(source: Editorial): EditorialPersistenceModel = {
    EditorialPersistenceModel(source.id, source.name, source.address)
  }
}

object PersistenceEditorialConverter extends ModelConverter[EditorialPersistenceModel, Editorial] {
  override def convert(source: EditorialPersistenceModel):Editorial  = {
    Editorial(source.id, source.name, source.address, List())
  }

  def convertWithRelations(source: EditorialPersistenceModel, books:Seq[Book]):Editorial  = {
    Editorial(source.id, source.name, source.address, books.map(BookMinConverter.convert))
  }
}

object EditorialMinConverter extends ModelConverter[Editorial, EditorialMin] {
  override def convert(source: Editorial):EditorialMin  = {
    EditorialMin(source.id, source.name, source.address)
  }
}

object MinEditorialConverter extends ModelConverter[EditorialMin, Editorial]{
  override def convert(source: EditorialMin):Editorial = {
    Editorial(source.id, source.name, source.address, List())
  }
}