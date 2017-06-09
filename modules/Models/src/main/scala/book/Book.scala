/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package book.model

import author.model.Author
import crud.models.Row
import review.model.Review
import editorial.model.Editorial

case class Book(id:Int, name:String, description:String, ISBN:String, image:String, reviews:Seq[Review], authors:Seq[Author], editorial: Option[Editorial]) extends Row