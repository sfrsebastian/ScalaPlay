/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package book.model

import author.model.Author
import crud.models.Row
import comment.model.Comment
import editorial.model.Editorial

case class Book(id:Int, name:String, description:String, ISBN:String, image:String, comments:Seq[Comment], authors:Seq[Author], editorial: Option[Editorial]) extends Row