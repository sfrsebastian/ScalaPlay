/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package author.model

import crud.models.Entity
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

class AuthorTable(tag:Tag) extends Entity[AuthorPersistenceModel](tag, "AUTHORS") {
  def lastName = column[String]("LAST_NAME")

  def * = (id, name, lastName) <> (AuthorPersistenceModel.tupled, AuthorPersistenceModel.unapply _)
}
