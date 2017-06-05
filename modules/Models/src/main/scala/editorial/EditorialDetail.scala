/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package editorial.model

import book.model.BookMin

case class EditorialDetail(id:Int, name:String, address:String, books:Seq[BookMin]) extends EditorialDTO