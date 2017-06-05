/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package review.model

import crud.models.ModelConverter
import book.model._

object ReviewPersistenceConverter extends ModelConverter[Review, ReviewPersistenceModel] {

  implicit def BookPersistenceModel2Book (t : BookPersistenceModel) : Book = BookPersistenceConverter.convertInverse(t)

  override def convert(source: Review): ReviewPersistenceModel = {
    ReviewPersistenceModel(source.id, source.name, source.content, source.book.id)
  }

  def convert(source:Review, bookId:Int): ReviewPersistenceModel ={
    convert(source).copy(bookId=bookId)
  }

  override def convertInverse(source: ReviewPersistenceModel):Review  = {
    Review(source.id, source.name, source.content, Book(source.bookId,"","","","",Seq(), Seq(), None))
  }

  def convertInverse(source:ReviewPersistenceModel, book:BookPersistenceModel):Review ={
    convertInverse(source).copy(book = book)
  }
}

object ReviewDetailConverter extends ModelConverter[Review, ReviewDetail] {

  implicit def Book2Min (t : Book) : BookMin = BookMinConverter.convert(t)

  implicit def Min2Book (t : BookMin) : Book = BookMinConverter.convertInverse(t)

  override def convert(source: Review):ReviewDetail  = {
    ReviewDetail(source.id, source.content, source.book:BookMin)
  }

  override def convertInverse(source: ReviewDetail):Review = {
    Review(source.id, "" , source.content, source.book:Book)
  }

  def convertInverse(source:ReviewDetail, book: Book) : Review ={
    convertInverse(source).copy(book = book)
  }
}

object ReviewMinConverter extends ModelConverter[Review, ReviewMin] {
  override def convert(source: Review): ReviewMin = {
    ReviewMin(source.id, source.content)
  }

  override def convertInverse(source: ReviewMin) : Review  = {
    Review(1, "", source.content, Book(1, "","","","",Seq(), Seq(), None))
  }
}