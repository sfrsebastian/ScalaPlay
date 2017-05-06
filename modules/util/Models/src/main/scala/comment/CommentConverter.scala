package comment.model

import crud.models.ModelConverter
import book.model._

/**
  * Created by sfrsebastian on 5/1/17.
  */
object CommentPersistenceConverter extends ModelConverter[Comment, CommentPersistenceModel] {
  override def convert(source: Comment): CommentPersistenceModel = {
    CommentPersistenceModel(source.id, source.name, source.content, source.book.id)
  }

  def convertCurried(source:Comment): (Int) => CommentPersistenceModel ={
    (bookId:Int) => convert(source).copy(bookId=bookId)
  }
}

object PersistenceCommentConverter extends ModelConverter[CommentPersistenceModel, Comment] {

  implicit def BookPersistenceModel2Book (t : BookPersistenceModel) : Book = PersistenceBookConverter.convert(t)

  override def convert(source: CommentPersistenceModel):Comment  = {
    Comment(source.id, source.name, source.content, BookMin(source.bookId,"","","","",1))
  }

  def convertCurried(source:CommentPersistenceModel): (BookPersistenceModel) => Comment ={
    (book:BookPersistenceModel) => convert(source).copy(book = BookMinConverter.convert(book:Book))
  }
}

object CommentMinConverter extends ModelConverter[Comment, CommentMin] {
  override def convert(source: Comment):CommentMin  = {
    CommentMin(source.id, source.name, source.content)
  }
}

object MinCommentConverter extends ModelConverter[CommentMin, Comment]{
  override def convert(source: CommentMin):Comment = {
    Comment(source.id, source.name, source.content, BookMin(1,"","","","", 1))
  }

  def convertCurried(source:CommentMin): (BookPersistenceModel) => Comment ={
    (book:BookPersistenceModel) => convert(source).copy(book = BookMinConverter.convert(PersistenceBookConverter.convert(book)))
  }
}