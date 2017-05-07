import author.model.{Author, AuthorPersistenceModel, AuthorTable}
import authorbook.model.{AuthorBookPersistenceModel, AuthorBookTable}
import book.model._
import book.persistence.BookPersistence
import comment.model.{CommentPersistenceModel, CommentTable}
import comment.persistence.CommentPersistence
import crud.tests.CrudPersistenceTestTrait
import editorial.model.{EditorialPersistenceModel, EditorialTable}
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery
import scala.util.Random

trait BookPersistenceTestTrait extends CrudPersistenceTestTrait[Book, BookPersistenceModel, BookTable]{

  override val tables = Seq(
    TableQuery[EditorialTable],
    TableQuery[BookTable],
    TableQuery[AuthorTable],
    TableQuery[CommentTable],
    TableQuery[AuthorBookTable]
  )

  override val persistence = new BookPersistence(new CommentPersistence)
  override var seedCollection: Seq[Book] = Nil

  override def generatePojo: Book = {
    val authors = for {
      _ <- 0 to 2
    }yield factory.manufacturePojo(classOf[Author]).copy(id = Random.nextInt(20) + 1)
    factory.manufacturePojo(classOf[Book]).copy(id = Random.nextInt(20) + 1, authors = authors, comments = Seq())
  }

  override implicit def Persistence2Model = BookPersistenceConverter

  var editorialCollection:Seq[EditorialPersistenceModel] = Nil
  def generateEditorialPojo() = factory.manufacturePojo(classOf[EditorialPersistenceModel])

  var authorCollection:Seq[AuthorPersistenceModel] = Nil
  def generateAuthorPojo() = factory.manufacturePojo(classOf[AuthorPersistenceModel])

  var authorBookCollection:Seq[AuthorBookPersistenceModel] = Nil
  def generateAuthorBookPojo() = factory.manufacturePojo(classOf[AuthorBookPersistenceModel]).copy(authorId = (Random.nextInt(20) + 1))

  var commentCollection:Seq[CommentPersistenceModel] = Nil
  def generateCommentPojo() = factory.manufacturePojo(classOf[CommentPersistenceModel]).copy(bookId = Random.nextInt(20) + 1)

  override def populateDatabase = {
    editorialCollection = Seq()
    seedCollection = Seq()
    authorCollection = Seq()
    commentCollection = Seq()
    authorBookCollection = Seq()

    editorialCollection = for {
      _ <- 0 to 19
    }yield generateEditorialPojo
    val action1 = TableQuery[EditorialTable] ++= editorialCollection

    seedCollection = for {
      i <- 0 to 19
    }yield generatePojo.copy(id = i+1)
    val action2 = TableQuery[BookTable] ++= seedCollection.map(b=>b:BookPersistenceModel)

    authorCollection = for {
      _ <- 0 to 19
    }yield generateAuthorPojo
    val action3 = TableQuery[AuthorTable] ++= authorCollection

    commentCollection = for {
      _ <- 0 to 100
    }yield generateCommentPojo
    val action4 = TableQuery[CommentTable] ++= commentCollection

    authorBookCollection = for {
      book <- seedCollection
      author <- book.authors
    }yield generateAuthorBookPojo.copy(bookId = book.id, authorId = author.id)
    val action5 = TableQuery[AuthorBookTable] ++= authorBookCollection

    DBIO.seq(action1, action2, action3, action4, action5)
  }

  override def assertByProperties(e1: BookPersistenceModel, e2: BookPersistenceModel): Unit = {
    super.assertByProperties(e1, e2)
    assert(e1.description == e2.description, "La descripcion deberia ser la misma")
    assert(e1.ISBN == e2.ISBN, "El ISBN deberia ser el mismo")
    assert(e1.image == e2.image, "La imagen deberia ser la misma")
  }

  override def createTest: Unit = {
    super.createTest

  }

}