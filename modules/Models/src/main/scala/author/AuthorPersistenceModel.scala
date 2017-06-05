/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package author.model

import crud.models.Row

case class AuthorPersistenceModel(id:Int, name:String, lastName:String) extends Row
