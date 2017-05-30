package book.logic

import javax.inject.Inject

import author.model.Author
import book.model.Book
import book.persistence.BookPersistenceTrait
import book.traits.AuthorBookLogicTrait
import layers.persistence.ManyToManyPersistence

/**
  * Created by sfrsebastian on 5/30/17.
  */
class AuthorBookLogic @Inject() (val persistence:BookPersistenceTrait with ManyToManyPersistence[Author, Book])extends AuthorBookLogicTrait