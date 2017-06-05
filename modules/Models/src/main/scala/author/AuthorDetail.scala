/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package author

import author.model.AuthorDTO
import book.model.BookMin

case class AuthorDetail(
  val id:Int,
  val name:String,
  val lastName:String,
  val books:Seq[BookMin]
) extends AuthorDTO
