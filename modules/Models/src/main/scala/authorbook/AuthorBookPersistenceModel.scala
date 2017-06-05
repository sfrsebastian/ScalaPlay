/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package authorbook.model

import crud.models.Row

case class AuthorBookPersistenceModel(id:Int, name:String, bookId:Int, authorId:Int) extends Row
