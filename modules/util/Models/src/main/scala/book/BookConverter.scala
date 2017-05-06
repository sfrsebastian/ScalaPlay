package book.model

import author.model.{Author, AuthorMinConverter, AuthorPersistenceModel, PersistenceAuthorConverter}
import crud.models.ModelConverter
import comment.model._
import editorial.model._
/**
  * Created by sfrsebastian on 4/30/17.
  */
object BookPersistenceConverter extends ModelConverter[Book, BookPersistenceModel] {
  override def convert(source: Book): BookPersistenceModel = {
    BookPersistenceModel(source.id, source.name, source.description, source.ISBN, source.image, source.editorial.id)
  }
}

object PersistenceBookConverter extends ModelConverter[BookPersistenceModel, Book]{

  implicit def CommentPersistenceModel2Comment (t : CommentPersistenceModel) : Comment = PersistenceCommentConverter.convert(t)

  implicit def AuthorPersistenceModel2Author (t : AuthorPersistenceModel) : Author = PersistenceAuthorConverter.convert(t)

  implicit def EditorialPersistenceModel2Editorial (t : EditorialPersistenceModel) : Editorial = PersistenceEditorialConverter.convert(t)

  override def convert(source: BookPersistenceModel) = {
    Book(source.id, source.name, source.description, source.ISBN, source.image, List(), List(), EditorialMin(1,"",""))
  }

  def convertWithRelations(source:BookPersistenceModel, comments: Seq[CommentPersistenceModel], authors:Seq[AuthorPersistenceModel], editorial:EditorialPersistenceModel): Book ={
    convert(source).copy(comments=comments.map(e=>CommentMinConverter.convert(e:Comment)), authors= authors.map(a=>AuthorMinConverter.convert(a:Author)), editorial = EditorialMinConverter.convert(editorial:Editorial))
  }
}

object BookMinConverter extends ModelConverter[Book, BookMin] {
  override def convert(source: Book):BookMin  = {
    BookMin(source.id, source.name, source.description, source.ISBN, source.image, source.editorial.id)
  }
}

object MinBookConverter extends ModelConverter[BookMin, Book]{
  override def convert(source: BookMin):Book = {
    Book(source.id, source.name, source.description, source.ISBN, source.image, List(), List(), EditorialMin(1,"",""))
  }

  def convertWithRelations(source:BookMin, comments: Seq[Comment], authors:Seq[Author], editorial:Editorial): Book ={
    convert(source).copy(comments = comments.map(c => CommentMinConverter.convert(c)), authors = authors.map(a=>AuthorMinConverter.convert(a)), editorial = EditorialMinConverter.convert(editorial))
  }
}
