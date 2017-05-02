package models.comment

import crud.models.ModelConverter
import models.book.Book

/**
  * Created by sfrsebastian on 5/1/17.
  */
object CommentPersistenceConverter extends ModelConverter[Comment, CommentPersistenceModel] {
  override def convert(source: Comment): CommentPersistenceModel = {
    CommentPersistenceModel(source.id, source.name, source.content, 1)
  }

  def convertCurried(source:Comment): (Int) => CommentPersistenceModel ={
    (bookId:Int) => convert(source).copy(bookId=bookId)
  }
}

object PersistenceCommentConverter extends ModelConverter[CommentPersistenceModel, Comment] {
  override def convert(source: CommentPersistenceModel):Comment  = {
    Comment(source.id, source.name, source.content)
  }
}