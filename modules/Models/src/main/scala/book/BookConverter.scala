package book.model

import author.model._
import crud.models.ModelConverter
import comment.model._
import editorial.model._
/**
  * Created by sfrsebastian on 4/30/17.
  */
object BookPersistenceConverter extends ModelConverter[Book, BookPersistenceModel] {

  implicit def CommentPersistenceModel2Comment (t : CommentPersistenceModel) : Comment = CommentPersistenceConverter.convertInverse(t)

  implicit def AuthorPersistenceModel2Author (t : AuthorPersistenceModel) : Author = AuthorPersistenceConverter.convertInverse(t)

  implicit def EditorialPersistenceModel2Editorial (t : EditorialPersistenceModel) : Editorial = EditorialPersistenceConverter.convertInverse(t)


  override def convertInverse(source: BookPersistenceModel) = {
    Book(source.id, source.name, source.description, source.ISBN, source.image, List(), List(), None)
  }

  def convertInverse(source:BookPersistenceModel, comments: Seq[CommentPersistenceModel], authors:Seq[AuthorPersistenceModel], editorial:Option[EditorialPersistenceModel]): Book ={
    convertInverse(source).copy(
        comments=comments.map(e => CommentPersistenceConverter.convertInverse(e, source)),
        authors= authors.map(a=>a:Author),
        editorial = editorial.map(e => e:Editorial)
      )
  }

  override def convert(source: Book): BookPersistenceModel = {
    BookPersistenceModel(source.id, source.name, source.description, source.ISBN, source.image, source.editorial.map(e=>e.id))
  }
}

object BookMinConverter extends ModelConverter[Book, BookMin] {

  override def convert(source: Book):BookMin  = {
    BookMin(
      source.id,
      source.name,
      source.description,
      source.ISBN,
      source.image
    )
  }

  override def convertInverse(source: BookMin):Book = {
    Book(
      source.id,
      source.name,
      source.description,
      source.ISBN,
      source.image,
      Seq(),
      Seq(),
      None
    )
  }

  def convertInverse(source:BookMin, comments: Seq[Comment], authors:Seq[Author], editorial:Option[Editorial]): Book ={
    convertInverse(source)
      .copy(
        comments = comments,
        authors = authors,
        editorial = editorial
      )
  }
}

object BookDetailConverter extends ModelConverter[Book, BookDetail] {
  implicit def Author2Min (t : Author) : AuthorMin = AuthorMinConverter.convert(t)
  implicit def Comment2Min (t : Comment) : CommentMin = CommentMinConverter.convert(t)
  implicit def Min2Author (t : AuthorMin) : Author = AuthorMinConverter.convertInverse(t)
  implicit def Min2Comment (t : CommentMin) : Comment = CommentMinConverter.convertInverse(t)
  implicit def Editorial2Min (t : Editorial) : EditorialMin = EditorialMinConverter.convert(t)
  implicit def Min2Editorial (t : EditorialMin) : Editorial = EditorialMinConverter.convertInverse(t)

  override def convert(source: Book): BookDetail = {
    BookDetail(source.id, source.name, source.description, source.ISBN, source.image, source.authors.map(a => a:AuthorMin), source.comments.map(c => c:CommentMin), source.editorial.map(e=>e:EditorialMin))
  }

  override def convertInverse(source: BookDetail) : Book  = {
    Book(source.id, source.name, source.description,source.ISBN,source.image, source.comments.map(c=>c:Comment), source.authors.map(a=>a:Author), source.editorial.map(e=>e:Editorial))
  }
}