/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package editorial.model

import book.model._
import crud.models.ModelConverter

object EditorialPersistenceConverter extends ModelConverter[Editorial, EditorialPersistenceModel] {

  override def convert(source: Editorial): EditorialPersistenceModel = {
    EditorialPersistenceModel(source.id, source.name, source.address)
  }

  override def convertInverse(source: EditorialPersistenceModel):Editorial  = {
    Editorial(source.id, source.name, source.address, List())
  }

  def convertInverse(source: EditorialPersistenceModel, books:Seq[Book]):Editorial  = {
    Editorial(source.id, source.name, source.address, books)
  }
}

object EditorialDetailConverter extends ModelConverter[Editorial, EditorialDetail] {

  implicit def Book2Min (t : Book) : BookMin = BookMinConverter.convert(t)
  implicit def Min2Book (t : BookMin) : Book = BookMinConverter.convertInverse(t)

  override def convert(source: Editorial):EditorialDetail  = {
    EditorialDetail(source.id, source.name, source.address, source.books.map(b => b:BookMin))
  }

  override def convertInverse(source: EditorialDetail):Editorial = {
    Editorial(source.id, source.name, source.address, source.books.map(b=>b:Book))
  }
}

object EditorialMinConverter extends ModelConverter[Editorial, EditorialMin] {

  override def convert(source: Editorial): EditorialMin = {
    EditorialMin(source.id, source.name, source.address)
  }

  override def convertInverse(source: EditorialMin) : Editorial  = {
    Editorial(source.id, source.name, source.address, List())
  }
}