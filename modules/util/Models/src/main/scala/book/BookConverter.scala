package book.model

import crud.models.ModelConverter
import comment.model._
/**
  * Created by sfrsebastian on 4/30/17.
  */
object BookPersistenceConverter extends ModelConverter[Book, BookPersistenceModel] {
  override def convert(source: Book): BookPersistenceModel = {
    BookPersistenceModel(source.id, source.name, source.description, source.ISBN, source.image)
  }
}

object PersistenceBookConverter extends ModelConverter[BookPersistenceModel, Book]{

  implicit def CommentPersistenceModel2Comment (t : CommentPersistenceModel) : Comment = PersistenceCommentConverter.convert(t)

  override def convert(source: BookPersistenceModel) = {
    Book(source.id, source.name, source.description, source.ISBN, source.image, List())
  }

  def convertCurried(source:BookPersistenceModel): (Seq[CommentPersistenceModel]) => Book ={
    (comments:Seq[CommentPersistenceModel]) => convert(source).copy(comments=comments.map(e=>CommentMinConverter.convert(e:Comment)))
  }
}

object BookMinConverter extends ModelConverter[Book, BookMin] {
  override def convert(source: Book):BookMin  = {
    BookMin(source.id, source.name, source.description, source.ISBN, source.image)
  }
}
