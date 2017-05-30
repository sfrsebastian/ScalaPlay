package controllers.author

import auth.controllers.AuthUserHandler
import author.logic.AuthorLogicTrait
import author.model.{Author, AuthorMin, AuthorPersistenceModel, AuthorTable}
import book.logic.BookLogicTrait
import book.model._
import comment.model.CommentMin
import crud.exceptions.{ServiceLayerException, TransactionException}
import editorial.model.EditorialMin
import layers.controllers.ManyToManyController
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller, Result}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 5/29/17.
  */
trait AuthorBookControllerTrait extends ManyToManyController[Author, AuthorPersistenceModel, AuthorTable, BookDetail, Book, BookPersistenceModel, BookTable] with AuthUserHandler {

  val sourceLogic:AuthorLogicTrait

  val destinationLogic:BookLogicTrait

  implicit val commentMinFormat = Json.format[CommentMin]

  implicit val authorMin = Json.format[AuthorMin]

  implicit val editorialMin = Json.format[EditorialMin]

  implicit val formatDetail = Json.format[BookDetail]

  implicit def Detail2Model = BookDetailConverter

  def relationMapper(author:Author):Seq[Book] = author.books

  def inverseRelationMapper(book:Book):Seq[Author] = book.authors
}
