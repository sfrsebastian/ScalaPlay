/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package book.model

import author.model._
import crud.models.ModelConverter
import review.model._
import editorial.model._

object BookPersistenceConverter extends ModelConverter[Book, BookPersistenceModel] {

  implicit def ReviewPersistenceModel2Review (t : ReviewPersistenceModel) : Review = ReviewPersistenceConverter.convertInverse(t)

  implicit def AuthorPersistenceModel2Author (t : AuthorPersistenceModel) : Author = AuthorPersistenceConverter.convertInverse(t)

  implicit def EditorialPersistenceModel2Editorial (t : EditorialPersistenceModel) : Editorial = EditorialPersistenceConverter.convertInverse(t)


  override def convertInverse(source: BookPersistenceModel) = {
    Book(source.id, source.name, source.description, source.ISBN, source.image, List(), List(), None)
  }

  def convertInverse(source:BookPersistenceModel, reviews: Seq[ReviewPersistenceModel], authors:Seq[AuthorPersistenceModel], editorial:Option[EditorialPersistenceModel]): Book ={
    convertInverse(source).copy(
        reviews=reviews.map(e => ReviewPersistenceConverter.convertInverse(e, source)),
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

  def convertInverse(source:BookMin, reviews: Seq[Review], authors:Seq[Author], editorial:Option[Editorial]): Book ={
    convertInverse(source)
      .copy(
        reviews = reviews,
        authors = authors,
        editorial = editorial
      )
  }
}

object BookDetailConverter extends ModelConverter[Book, BookDetail] {
  implicit def Author2Min (t : Author) : AuthorMin = AuthorMinConverter.convert(t)
  implicit def Review2Min (t : Review) : ReviewMin = ReviewMinConverter.convert(t)
  implicit def Min2Author (t : AuthorMin) : Author = AuthorMinConverter.convertInverse(t)
  implicit def Min2Review (t : ReviewMin) : Review = ReviewMinConverter.convertInverse(t)
  implicit def Editorial2Min (t : Editorial) : EditorialMin = EditorialMinConverter.convert(t)
  implicit def Min2Editorial (t : EditorialMin) : Editorial = EditorialMinConverter.convertInverse(t)

  override def convert(source: Book): BookDetail = {
    BookDetail(source.id, source.name, source.description, source.ISBN, source.image, source.authors.map(a => a:AuthorMin), source.reviews.map(c => c:ReviewMin), source.editorial.map(e=>e:EditorialMin))
  }

  override def convertInverse(source: BookDetail) : Book  = {
    Book(source.id, source.name, source.description,source.ISBN,source.image, source.reviews.map(c=>c:Review), source.authors.map(a=>a:Author), source.editorial.map(e=>e:Editorial))
  }
}