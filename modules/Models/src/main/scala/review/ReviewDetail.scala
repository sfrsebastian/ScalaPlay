/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package review.model

import book.model.BookMin

case class ReviewDetail(id:Int, content:String, book:BookMin) extends ReviewDTO