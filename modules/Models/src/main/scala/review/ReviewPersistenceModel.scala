/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package review.model

import crud.models.Row

case class ReviewPersistenceModel(id:Int, name:String, content:String, bookId:Int) extends Row
