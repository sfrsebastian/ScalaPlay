/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package book.logic

import author.model.Author
import book.model.Book
import book.persistence.BookPersistenceTrait
import book.traits.AuthorBookLogicTrait
import com.google.inject.Inject
import layers.persistence.ManyToManyPersistence

class AuthorBookLogic @Inject() (val persistence:BookPersistenceTrait with ManyToManyPersistence[Author, Book])extends AuthorBookLogicTrait