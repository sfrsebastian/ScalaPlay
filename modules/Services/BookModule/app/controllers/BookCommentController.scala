package controllers.book

import book.logic.BookLogicTrait
import book.model.BookMin
import com.google.inject.Inject
import comment.logic.CommentLogicTrait
import comment.model._
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 5/15/17.
  */
class BookCommentController @Inject() (val bookLogic:BookLogicTrait, val commentLogic:CommentLogicTrait) extends Controller{

  implicit val formatBookMin = Json.format[BookMin]

  implicit val formatDetail = Json.format[CommentDetail]

  implicit def Detail2Model (m : CommentDetail) : Comment = CommentDetailConverter.convertInverse(m)

  implicit def Model2Detail (s: Comment):CommentDetail = CommentDetailConverter.convert(s)

  def getBookComments(bookId:Int, start:Option[Int], limit:Option[Int]) = Action.async {
    for{
      result <- bookLogic.get(bookId)
    }yield {
      result match {
        case Some(book) => Ok(Json.toJson(book.comments.map(e => e:CommentDetail)))
        case None => NotFound("No se encontró el libro solicitado")
      }
    }
  }

  def getBookComment(bookId:Int, commentId:Int) = Action.async{
    for{
      book <- bookLogic.get(bookId)
    }yield {
      val result = book.flatMap(b => b.comments.find(_.id == commentId))
      result match {
        case Some(comment) => Ok(Json.toJson(comment:CommentDetail))
        case None => NotFound("No se encontró el comentario solicitado")
      }
    }
  }

  def createBookComment(bookId:Int) = Action.async(parse.json){request =>
    request.body.validateOpt[CommentDetail].getOrElse(None) match {
      case Some(x) => commentLogic.create(x.copy(book = x.book.copy(id=bookId)):Comment).map{
        case Some(element) => Created(Json.toJson(element:CommentDetail))
        case None => BadRequest("El recurso no pudo ser creado")
      }
      case None => Future(BadRequest("Error en formato de contenido"))
    }
  }
}
