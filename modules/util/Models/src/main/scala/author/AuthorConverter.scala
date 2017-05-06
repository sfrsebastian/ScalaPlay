package author.model

import book.model._
import crud.models.ModelConverter
/**
  * Created by sfrsebastian on 4/30/17.
  */
object AuthorPersistenceConverter extends ModelConverter[Author, AuthorPersistenceModel] {
  override def convert(source: Author): AuthorPersistenceModel = {
    AuthorPersistenceModel(source.id, source.name, source.lastName)
  }
}

object PersistenceAuthorConverter extends ModelConverter[AuthorPersistenceModel, Author]{

  implicit def BookPersistenceModel2Book (t : BookPersistenceModel) : Book = PersistenceBookConverter.convert(t)

  override def convert(source: AuthorPersistenceModel) = {
    Author(source.id, source.name, source.lastName, List())
  }

  def convertWithRelations(source: AuthorPersistenceModel, books:Seq[BookPersistenceModel]) = {
    Author(source.id, source.name, source.lastName, books.map(b=> BookMinConverter.convert(b:Book)))
  }
}

object AuthorMinConverter extends ModelConverter[Author, AuthorMin] {
  override def convert(source: Author):AuthorMin  = {
    AuthorMin(source.id, source.name, source.lastName)
  }
}
