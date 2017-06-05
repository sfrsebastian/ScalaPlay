/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package comment.model

import book.model.BookMin

case class CommentDetail(id:Int, content:String, book:BookMin) extends CommentDTO