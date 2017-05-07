package comment.model

import crud.models.ModelConverter
import book.model._

/**
  * Created by sfrsebastian on 5/1/17.
  */
object CommentPersistenceConverter extends ModelConverter[Comment, CommentPersistenceModel] {

  implicit def BookPersistenceModel2Book (t : BookPersistenceModel) : Book = BookPersistenceConverter.convertInverse(t)

  override def convert(source: Comment): CommentPersistenceModel = {
    CommentPersistenceModel(source.id, source.name, source.content, source.book.id)
  }

  def convert(source:Comment, bookId:Int): CommentPersistenceModel ={
    convert(source).copy(bookId=bookId)
  }

  override def convertInverse(source: CommentPersistenceModel):Comment  = {
    Comment(source.id, source.name, source.content, Book(source.bookId,"","","","",Seq(), Seq(), None))
  }

  def convertInverse(source:CommentPersistenceModel, book:BookPersistenceModel):Comment ={
    convertInverse(source).copy(book = book)
  }
}

object CommentMinConverter extends ModelConverter[Comment, CommentMin] {

  implicit def Book2Min (t : Book) : BookMin = BookMinConverter.convert(t)

  implicit def Min2Book (t : BookMin) : Book = BookMinConverter.convertInverse(t)

  override def convert(source: Comment):CommentMin  = {
    CommentMin(source.id, source.content)
  }

  override def convertInverse(source: CommentMin):Comment = {
    Comment(source.id, "" , source.content, Book(1,"","","","",Seq(),Seq(),None))
  }

  def convertInverse(source:CommentMin, book: Book) : Comment ={
    convertInverse(source).copy(book = book)
  }
}

object CommentFormConverter extends ModelConverter[Comment, CommentForm] {
  override def convert(source: Comment): CommentForm = {
    CommentForm(source.content, source.book.id)
  }

  override def convertInverse(source: CommentForm) : Comment  = {
    Comment(1, "", source.content, Book(source.bookId, "","","","",Seq(), Seq(), None))
  }
}