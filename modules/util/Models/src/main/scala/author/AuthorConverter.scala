package author.model

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
  override def convert(source: AuthorPersistenceModel) = {
    Author(source.id, source.name, source.lastName, List())
  }
}

object AuthorMinConverter extends ModelConverter[Author, AuthorMin] {
  override def convert(source: Author):AuthorMin  = {
    AuthorMin(source.id, source.name, source.lastName)
  }
}
