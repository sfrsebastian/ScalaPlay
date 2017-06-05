/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package book.model

import author.model.AuthorMin
import review.model.ReviewMin
import editorial.model.EditorialMin

case class BookDetail(
  val id:Int,
  val name:String,
  val description:String,
  val ISBN:String,
  val image:String,
  val authors:Seq[AuthorMin],
  val reviews:Seq[ReviewMin],
  val editorial:Option[EditorialMin]
) extends BookDTO
