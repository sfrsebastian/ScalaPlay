import play.api._
import slick.jdbc.meta.MTable
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import logic.bookModule.BookLogic
import models.bookModule.{Book, Books}

object Global extends GlobalSettings {
  def db: Database = Database.forConfig("mydb")
  val books = TableQuery[Books]
  val tables = List(books)

  override def onStart(app: Application) {
    if(app.configuration.getBoolean("dropCreate").getOrElse(false)){
      dropCreate
      seed()
    }
    else{
      create
    }
  }

  def create = {
    val existing = db.run(MTable.getTables)
    val f = existing.flatMap( v => {
      val names = v.map(mt => mt.name.name)
      val createIfNotExist = tables.filter( table =>
        (!names.contains(table.baseTableRow.tableName)))
        .map(table => {
          table.schema.create
        })
      db.run(DBIO.sequence(createIfNotExist))
    })
    Await.result(f, Duration.Inf)
  }

  def dropCreate = {
    val existing = db.run(MTable.getTables)
    val f = existing.flatMap( v => {
      val names = v.map(mt => mt.name.name)
      val createIfNotExist = tables.filter( table =>
        (names.contains(table.baseTableRow.tableName)))
        .map(table => {
          table.schema.drop.andThen(table.schema.create)
        })
      db.run(DBIO.sequence(createIfNotExist))
    })
    Await.result(f, Duration.Inf)
  }

  def seed(): Unit = {
    val books = Seq(
      Book(1L, "Cien anios de soledad", "Descripcion", "ISBN1", "url1"),
      Book(1L, "Cien anios de soledad2", "Descripcion2", "ISBN2", "url2")
    )
    books.map(BookLogic.create)
  }
}

