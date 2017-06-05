/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package editorial.model

import crud.models.Row

case class EditorialPersistenceModel(id:Int, name:String, address:String) extends Row
