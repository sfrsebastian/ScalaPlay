/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package authorbook

import author.model.Author
import book.model.Book
import crud.models.Row

case class AuthorBook(id:Int, name:String, author:Author, book:Book) extends Row
