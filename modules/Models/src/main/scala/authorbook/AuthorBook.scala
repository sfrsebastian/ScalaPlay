package authorbook

import author.model.Author
import book.model.Book
import crud.models.Row

/**
  * Created by sfrsebastian on 5/5/17.
  */
case class AuthorBook(id:Int, name:String, author:Author, book:Book) extends Row
