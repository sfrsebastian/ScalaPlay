/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package controllers.traits

import auth.controllers.AuthUserHandler
import author.model.AuthorMin
import book.model._
import review.model.ReviewMin
import editorial.model.{Editorial, EditorialMin, EditorialPersistenceModel, EditorialTable}
import layers.controllers.OneToManyController
import play.api.libs.json.Json

trait EditorialBookControllerTrait extends OneToManyController[Editorial, EditorialPersistenceModel, EditorialTable, BookDetail, Book, BookPersistenceModel, BookTable] with AuthUserHandler{
  implicit val ReviewMinFormat = Json.format[ReviewMin]

  implicit val authorMin = Json.format[AuthorMin]

  implicit val editorialMin = Json.format[EditorialMin]

  implicit val formatDetail = Json.format[BookDetail]

  implicit val Model2Detail = BookDetailConverter

  def relationMapper(editorial:Editorial):Seq[Book] = editorial.books

  override val originNotFound:String = "La editorial dada no existe"

  override val destinationNotFound:String = "El libro dado no existe"

  override val destinationNotAssociated:String = "El libro no se encuentra asociado a la editorial dada"

  override val errorAssociatingDestination:String = "Se presento un error asociando el libro a la editorial"

  override val errorDisassociatingDestination:String = "Se presento un error desasociando el libro de la editorial"
}
