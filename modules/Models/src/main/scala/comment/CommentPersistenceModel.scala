/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package comment.model

import crud.models.Row

case class CommentPersistenceModel(id:Int, name:String, content:String, bookId:Int) extends Row
