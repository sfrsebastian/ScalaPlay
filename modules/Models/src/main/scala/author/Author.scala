/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package author.model

import crud.models.Row
import book.model.Book

case class Author(id:Int, name:String, lastName:String, books:Seq[Book]) extends Row