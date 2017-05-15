package controllers.book

import author.AuthorDetail
import author.logic.AuthorLogicTrait
import author.model.{Author, AuthorDetailConverter, AuthorMin}
import book.model.BookMin
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
class BookAuthorController@Inject()(val authorLogic:AuthorLogicTrait) extends Controller{

  implicit val commentMinFormat = Json.format[CommentMin]

  implicit val editorialMin = Json.format[EditorialMin]

  implicit val authorMin = Json.format[AuthorMin]

  implicit val bookMin = Json.format[BookMin]

  implicit val formatDetail = Json.format[AuthorDetail]

  implicit def Detail2Author (m : AuthorDetail) : Author = AuthorDetailConverter.convertInverse(m)

  implicit def Author2Detail (s: Author): AuthorDetail  = AuthorDetailConverter.convert(s)

  def getAuthorsFromBook(bookId:Int) = Action.async {
    authorLogic.getAuthorsFromBook(bookId).map(authors => Ok(Json.toJson(authors.map(e=>e:AuthorDetail))))
  }

  def getAuthorFromBook(bookId:Int, authorId:Int) = Action.async{
    authorLogic.get(authorId).map {
      case Some(author) if author.books.map(_.id).contains(bookId) => Ok(Json.toJson(author: AuthorDetail))
      case _ => NotFound("No se encontró el autor solicitado")
    }
  }

  def addAuthorToBook(bookId:Int, authorId:Int) = Action.async{
    authorLogic.addAuthorToBook(bookId, authorId).map{
      case Some(book) => Created(Json.toJson(book:AuthorDetail))
      case None => BadRequest("Error asociando autor a libro")
    }
  }

  def replaceAuthorsFromBook(bookId:Int) = Action.async(parse.json){request =>
    request.body.validateOpt[Seq[AuthorDetail]].getOrElse(None) match {
      case Some(x) => authorLogic.replaceAuthorsFromBook(bookId,x.map(b=>b:Author)).map(authors => Created(Json.toJson(authors.map(b => b:AuthorDetail))))
      case None => Future(BadRequest("Error en formato de contenido"))
    }
  }

  def deleteAuthorFromBook(bookId:Int, authorId:Int) = Action.async{
    for{
      deleted <- authorLogic.removeAuthorFromBook(bookId, authorId:Int)
    }yield{
      deleted match {
        case Some(x) => Ok(Json.toJson(x:AuthorDetail))
        case _ => BadRequest("El autor no fue eliminado del libro")
      }
    }
  }
}
