package controllers.author

import author.model.AuthorMin
import book.logic.BookLogicTrait
import book.model.{Book, BookDetail, BookDetailConverter}
import com.google.inject.Inject
import comment.model.CommentMin
import editorial.model.EditorialMin
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 5/15/17.
  */
class AuthorBookController@Inject()(val bookLogic:BookLogicTrait) extends Controller{

  implicit val commentMinFormat = Json.format[CommentMin]

  implicit val authorMin = Json.format[AuthorMin]

  implicit val editorialMin = Json.format[EditorialMin]

  implicit val formatDetail = Json.format[BookDetail]

  implicit def Detail2Book (m : BookDetail) : Book = BookDetailConverter.convertInverse(m)

  implicit def Book2Detail (s: Book): BookDetail  = BookDetailConverter.convert(s)

  def getBooksFromAuthor(authorId:Int) = Action.async {
    bookLogic.getBooksFromAuthor(authorId).map(books => Ok(Json.toJson(books.map(e=>e:BookDetail))))
  }

  def getBookFromAuthor(authorId:Int, bookId:Int) = Action.async{
    bookLogic.get(bookId).map {
      case Some(book) if book.authors.map(_.id).contains(authorId) => Ok(Json.toJson(book: BookDetail))
      case _ => NotFound("No se encontró el libro solicitado")
    }
  }

  def addBookToAuthor(authorId:Int, bookId:Int) = Action.async{
    bookLogic.addBookToAuthor(authorId, bookId).map{
      case Some(book) => Created(Json.toJson(book:BookDetail))
      case None => BadRequest("Error asociando libro a autor")
    }
  }

  def replaceBooksFromAuthor(authorId:Int) = Action.async(parse.json){request =>
    request.body.validateOpt[Seq[BookDetail]].getOrElse(None) match {
      case Some(x) => bookLogic.replaceBooksFromAuthor(authorId,x.map(b=>b:Book)).map(books => Created(Json.toJson(books.map(b => b:BookDetail))))
      case None => Future(BadRequest("Error en formato de contenido"))
    }
  }

  def deleteBookFromAuthor(authorId:Int, bookId:Int) = Action.async{
    for{
      deleted <- bookLogic.removeBookFromAuthor(authorId, bookId)
    }yield{
      deleted match {
        case Some(x) => Ok(Json.toJson(x:BookDetail))
        case _ => BadRequest("El libro no fue eliminado del autor")
      }
    }
  }
}
