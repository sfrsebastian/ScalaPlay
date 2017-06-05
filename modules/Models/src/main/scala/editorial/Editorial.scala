/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package editorial.model

import book.model.Book
import crud.models.Row

case class Editorial(id:Int, name:String, address:String, books: Seq[Book]) extends Row