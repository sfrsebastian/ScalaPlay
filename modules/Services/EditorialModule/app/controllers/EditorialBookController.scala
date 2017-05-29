package controllers.editorial

import author.model.AuthorMin
import book.logic.BookLogicTrait
import book.model.{Book, BookDetail, BookDetailConverter}
import com.google.inject.Inject
import comment.model.CommentMin
import editorial.model.EditorialMin
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by sfrsebastian on 5/15/17.
  */
class EditorialBookController @Inject() (val bookLogic:BookLogicTrait) extends Controller{

  implicit val commentMinFormat = Json.format[CommentMin]

  implicit val authorMin = Json.format[AuthorMin]

  implicit val editorialMin = Json.format[EditorialMin]

  implicit val formatDetail = Json.format[BookDetail]

  implicit def Detail2Book (m : BookDetail) : Book = BookDetailConverter.convertInverse(m)

  implicit def Book2Detail (s: Book): BookDetail  = BookDetailConverter.convert(s)

  def getBooksFromEditorial(editorialId:Int) = Action.async {
    bookLogic.getBooksFromEditorial(editorialId).map(books => Ok(Json.toJson(books.map(e=>e:BookDetail))))
  }

  def getBookFromEditorial(editorialId:Int, bookId:Int) = Action.async{
    bookLogic.get(bookId).map(b => {
      b match {
        case Some(book) if book.editorial.map(_.id == editorialId).getOrElse(false) => Ok(Json.toJson(book:BookDetail))
        case _ => NotFound("No se encontró el libro solicitado")
      }
    })
  }

  def addBookToEditorial(editorialId:Int, bookId:Int) = Action.async{
    bookLogic.addBookToEditorial(editorialId, bookId).map{
      case Some(book) => Created(Json.toJson(book:BookDetail))
      case None => BadRequest("Error asociando libro a editorial")
    }
  }

  def removeBookFromEditorial(editorialId:Int, bookId:Int) = Action.async{
    bookLogic.removeBookFromEditorial(editorialId, bookId).map{
      case Some(book) => Created(Json.toJson(book:BookDetail))
      case None => BadRequest("Error desasociando libro a editorial")
    }
  }
}
