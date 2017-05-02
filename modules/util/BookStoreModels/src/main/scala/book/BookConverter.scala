package models.book

import crud.models.ModelConverter
import models.comment.{Comment, CommentPersistenceModel,PersistenceCommentConverter}
/**
  * Created by sfrsebastian on 4/30/17.
  */
object BookPersistenceConverter extends ModelConverter[Book, BookPersistenceModel] {
  override def convert(source: Book): BookPersistenceModel = {
    BookPersistenceModel(source.id, source.name, source.description, source.ISBN, source.image)
  }
}

object PersistenceBookConverter extends ModelConverter[BookPersistenceModel, Book]{
  implicit def Persistence2Model = PersistenceCommentConverter
  implicit def S2T (comment : CommentPersistenceModel)(implicit converter : ModelConverter[CommentPersistenceModel, Comment]) : Comment = converter.convert(comment)

  override def convert(source: BookPersistenceModel) = {
    Book(source.id, source.name, source.description, source.ISBN, source.image, List())
  }

  def convertCurried(source:BookPersistenceModel): (Seq[CommentPersistenceModel]) => Book ={
    (comments:Seq[CommentPersistenceModel]) => convert(source).copy(comments=comments.map(e=>e:Comment))
  }
}

object BookMinConverter extends ModelConverter[Book, BookMin] {
  override def convert(source: Book):BookMin  = {
    BookMin(source.id, source.name, source.description, source.ISBN, source.image)
  }
}
