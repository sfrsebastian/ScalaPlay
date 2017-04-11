import play.api._
import slick.jdbc.meta.MTable
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import models.bookModule.{Book, Books}

object Global extends GlobalSettings{

  val db = Database.forConfig("PostgresDB")
  val dbTest = Database.forConfig("PostgresDBTest")
  val books = TableQuery[Books]
  val tables = List(books)

  override def onStart(app: Application) {
    if(app.configuration.getBoolean("dropCreate").getOrElse(false)){
      dropCreate(db)
      dropCreate(dbTest)
    }
    else{
      create(db)
      create(dbTest)
    }
  }

  def create(database:Database) = {
    val existing = database.run(MTable.getTables)
    def f = existing.flatMap( v => {
      val names = v.map(mt => mt.name.name)
      val createIfNotExist = tables.filter( table =>
        (!names.contains(table.baseTableRow.tableName)))
        .map(table => {
          table.schema.create
        })
      database.run(DBIO.sequence(createIfNotExist))
    })
    Await.result(f, Duration.Inf)
  }

  def dropCreate(database:Database) = {
    val existing = database.run(MTable.getTables)
    val f = existing.flatMap( v => {
      val names = v.map(mt => mt.name.name)
      val createIfNotExist = tables.filter( table =>
        (names.contains(table.baseTableRow.tableName)))
        .map(table => {
          table.schema.drop.andThen(table.schema.create)
        })
      database.run(DBIO.sequence(createIfNotExist))
    })
    Await.result(f, Duration.Inf)
  }
}

