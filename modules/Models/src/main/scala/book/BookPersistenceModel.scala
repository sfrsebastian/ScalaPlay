/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package book.model

import crud.models.Row

case class BookPersistenceModel(id:Int, name:String, description:String, ISBN:String, image:String, editorialId: Option[Int] = None) extends Row
