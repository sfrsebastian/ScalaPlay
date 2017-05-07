package persistence

import author.model.{Author, AuthorPersistenceConverter, AuthorPersistenceModel, AuthorTable}
import authorbook.AuthorBook
import authorbook.model.{AuthorBookPersistenceModel, AuthorBookTable}
import book.model.{Book, BookPersistenceConverter, BookPersistenceModel, BookTable}
import comment.model.{Comment, CommentPersistenceConverter, CommentPersistenceModel, CommentTable}
import editorial.model.{Editorial, EditorialPersistenceConverter, EditorialPersistenceModel, EditorialTable}
import slick.lifted.TableQuery
import uk.co.jemos.podam.api.PodamFactoryImpl
import slick.jdbc.PostgresProfile.api._
import scala.util.Random

/**
  * Created by sfrsebastian on 5/7/17.
  */
object DatabasePopulator {

  val factory = new PodamFactoryImpl

  implicit def editorial2Persistence (s : Editorial):EditorialPersistenceModel = EditorialPersistenceConverter.convert(s)
  implicit def book2Persistence (s : Book):BookPersistenceModel = BookPersistenceConverter.convert(s)
  implicit def author2Persistence (s : Author):AuthorPersistenceModel = AuthorPersistenceConverter.convert(s)
  implicit def comment2Persistence (s : Comment):CommentPersistenceModel = CommentPersistenceConverter.convert(s)

  val tables = Seq(
    TableQuery[EditorialTable],
    TableQuery[BookTable],
    TableQuery[AuthorTable],
    TableQuery[CommentTable],
    TableQuery[AuthorBookTable]
  )

  var editorials:Seq[Editorial] = Seq()
  var books:Seq[Book] = Seq()
  var authors:Seq[Author] = Seq()
  var authorBook:Seq[AuthorBook] = Seq()
  var comments:Seq[Comment] = Seq()

  def generateEditorial(id:Int) = factory.manufacturePojo(classOf[Editorial]).copy(id=id)
  def generateBook(id:Int) = factory.manufacturePojo(classOf[Book]).copy(id = id, editorial = Some(editorials(Random.nextInt(editorials.length))), comments = Seq())
  def generateAuthor(id:Int) = factory.manufacturePojo(classOf[Author]).copy(id = id, books = Seq())
  def generateAuthorBook(book:Book, author:Author) = factory.manufacturePojo(classOf[AuthorBook]).copy(book = book, author = author)
  def generateComment = factory.manufacturePojo(classOf[Comment])copy(book = books(Random.nextInt(books.length)))

  def populate = {
    editorials = for {
      i <- 0 to 4
    }yield generateEditorial(i+1)
    val editorialAction = TableQuery[EditorialTable] ++= editorials.map(e=>e:EditorialPersistenceModel)

    books = for {
      i <- 0 to 19
    }yield generateBook(i+1)
    val bookAction = TableQuery[BookTable] ++= books.map(e=>e:BookPersistenceModel)

    authors = for {
      i <- 0 to 19
    }yield generateAuthor(i+1)
    val authorAction = TableQuery[AuthorTable] ++= authors.map(e=>e:AuthorPersistenceModel)

    authorBook = for {
      book <- books
      bookAuthors <- Random.shuffle(authors).take(Random.nextInt(3) + 1).map(Some(_))
      author <- bookAuthors
    }yield {
      generateAuthorBook(book, author)
    }
    val authorBookAction = TableQuery[AuthorBookTable] ++= authorBook.map(e=>AuthorBookPersistenceModel(1,"",e.book.id, e.author.id))

    comments = for {
      _ <- 0 to 99
    }yield generateComment

    val commentAction = TableQuery[CommentTable] ++= comments.map(e=>e:CommentPersistenceModel)

    DBIO.seq(editorialAction, bookAction, authorAction, authorBookAction, commentAction)
  }
}
