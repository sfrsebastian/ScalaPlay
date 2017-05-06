package author.model

import author.AuthorForm
import book.model._
import crud.models.ModelConverter
/**
  * Created by sfrsebastian on 4/30/17.
  */
object AuthorPersistenceConverter extends ModelConverter[Author, AuthorPersistenceModel] {
  implicit def BookPersistenceModel2Book (t : BookPersistenceModel) : Book = BookPersistenceConverter.convertInverse(t)

  override def convert(source: Author): AuthorPersistenceModel = {
    AuthorPersistenceModel(source.id, source.name, source.lastName)
  }

  override def convertInverse(source: AuthorPersistenceModel) = {
    Author(source.id, source.name, source.lastName, List())
  }

  def convertInverse(source: AuthorPersistenceModel, books:Seq[BookPersistenceModel]) = {
    Author(source.id, source.name, source.lastName, books.map(b=> b:Book))
  }
}

object AuthorMinConverter extends ModelConverter[Author, AuthorMin] {
  implicit def Book2Min (t : Book) : BookMin = BookMinConverter.convert(t)
  implicit def Min2Book (t : BookMin) : Book = BookMinConverter.convertInverse(t)

  override def convert(source: Author) : AuthorMin  = {
    AuthorMin(source.id, source.name, source.lastName, books = source.books.map(b=>b:BookMin))
  }

  override def convertInverse(source: AuthorMin): Author = {
    Author(source.id, source.name, source.lastName, source.books.map(b=>b:Book))
  }
}

object AuthorFormConverter extends ModelConverter[Author, AuthorForm] {

  override def convert(source: Author): AuthorForm = {
    AuthorForm(source.name, source.lastName)
  }

  override def convertInverse(source: AuthorForm) : Author  = {
    Author(1, source.name, source.lastName, List())
  }
}