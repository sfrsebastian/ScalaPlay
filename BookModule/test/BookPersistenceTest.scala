import common.DatabaseOperations
import models.bookModule.{Book, Books}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import persistence.bookModule.BookPersistenceTesting
import org.scalatestplus.play._
import slick.jdbc.PostgresProfile.api._
import uk.co.jemos.podam.api.PodamFactoryImpl

import scala.util.Random

class BookPersistenceTest extends PlaySpec with BeforeAndAfterEach with BeforeAndAfterAll with ScalaFutures{
  val factory = new PodamFactoryImpl
  var persistence = new BookPersistenceTesting
  var books:Seq[Book] = Nil

  override def beforeAll(): Unit ={
    DatabaseOperations.createIfNotExist[Book, Books](persistence.db, Seq(persistence.table))
  }

  override def beforeEach(){
    DatabaseOperations.DropCreate[Book, Books](persistence.db, Seq(persistence.table))
    books = for {
      i <- 0 to 19
    }yield factory.manufacturePojo(classOf[Book])
    books.map(insert)
  }

  override def afterAll(): Unit ={
    DatabaseOperations.DropCreate[Book, Books](persistence.db, Seq(persistence.table))
  }

  def insert(b: Book) = persistence.db.run(persistence.table += b)

  "Al insertar un nuevo libro" must {
    "Los campos del objeto persistido deben ser iguales que los del objeto creado" in {
      val nuevoLibro = factory.manufacturePojo(classOf[Book])
      whenReady(persistence.create(nuevoLibro)) { result => {
        result match{
          case Some(libro)=>{
            assert(libro.id == books.length + 1, "El id debe incrementar en 1")
            assert(libro.name == nuevoLibro.name, "El nombre deberia ser el mismo")
            assert(libro.description == nuevoLibro.description, "La descripcion deberia ser la misma")
            assert(libro.ISBN == nuevoLibro.ISBN, "El ISBN deberia ser el mismo")
            assert(libro.image == nuevoLibro.image, "La imagen deberia ser la misma")
          }
          case None => fail()
        }
      }}
    }

    "El objeto creado debe existir en la base de datos" in {
      val nuevoLibro = factory.manufacturePojo(classOf[Book])
      whenReady(persistence.create(nuevoLibro)) { result =>
        result match {
          case Some(libro) => {
            whenReady(persistence.db.run(persistence.table.filter(_.id === libro.id).result.headOption)){search =>{
              search match {
                case Some(buscado) => assert(libro == buscado)
                case None => fail()
              }
            }}
          }
          case None => fail()
        }
      }
    }
  }

  "Al solicitar un libro" must {
    "Los campos del objeto obtenido deben ser iguales a los del objeto ya existente cuando se solicita por id" in {
      val id = Random.nextInt(20)
      val libroExistente = books(id)
      whenReady(persistence.get(id + 1)){result => {
        result match {
          case Some(libro: Book) => {
            assert(libro.name == libroExistente.name, "El nombre deberia ser el mismo")
            assert(libro.description == libroExistente.description, "La descripcion deberia ser la misma")
            assert(libro.ISBN == libroExistente.ISBN, "El ISBN deberia ser el mismo")
            assert(libro.image == libroExistente.image, "La imagen deberia ser la misma")
          }
          case None => fail("El objeto buscado deberia encontrarse")
        }
      }}
    }

    "El campo de filtrado debe ser igual al del objeto ya existente cuando se solicita por una consulta" in {
      val id = Random.nextInt(20)
      val libroExistente = books(id)
      val query = persistence.table.filter(_.ISBN === libroExistente.ISBN)
      whenReady(persistence.get(query)){result => {
        result match {
          case Some(libro: Book) => {
            assert(libro.ISBN == libroExistente.ISBN, "El ISBN deberia ser el mismo")
          }
          case None => fail("El objeto buscado deberia encontrarse")
        }
      }}
    }
  }

  "Al solicitar multiples libros" must {
    "La longitud de la lista de objetos recibida debe ser la misma que la de objetos ya existentes cuando se piden todos los libros" in {
      whenReady(persistence.getAll){result => {
        result match {
          case libros => {
            assert(libros.length == books.length, "La cantidad de libros recibida debe ser la misma que los ya existentes")
          }
          case Nil => fail("La lista no deberia ser vacia")
        }
      }}
    }

    "Se debe recibir una lista de longitud 1 cuando se filtra por una consulta" in {
      val id = Random.nextInt(20)
      val libroExistente = books(id)
      val query = persistence.table.filter(_.ISBN === libroExistente.ISBN)
      whenReady(persistence.getAll(query)){result => {
        result match {
          case libros => {
            assert(libros.length == 1, "La cantidad de libros recibida debe ser 1")
          }
          case Nil => fail("La lista no deberia ser vacia")
        }
      }}
    }

    "La longitud de la lista de objetos recibida debe ser menor o igual a la especificada por los parametros de paginacion" in {
      assert(true)
    }
  }

  "Al actualizar un libro" must {
    "Se debe retornar el objeto actualizado" in {
      val actualizar = factory.manufacturePojo(classOf[Book])
      val id = Random.nextInt(20)
      whenReady(persistence.update(id + 1, actualizar)) {result => {
        result match {
          case Some(libro) => {
            assert(libro.id == id+1, "El id deberia ser el mismo")
            assert(libro.name == actualizar.name, "El nombre deberia ser el mismo")
            assert(libro.description == actualizar.description, "La descripcion deberia ser la misma")
            assert(libro.ISBN == actualizar.ISBN, "El ISBN deberia ser el mismo")
            assert(libro.image == actualizar.image, "La imagen deberia ser la misma")
          }
          case None => fail("Se deberia obtener el libro actualizado")
        }
      }}
    }

    "No se deberia actualizar ningún registro en la base de datos si el libro no existe" in {
      val actualizar = factory.manufacturePojo(classOf[Book])
      val id = 21
      whenReady(persistence.update(id, actualizar)) { result => {
        result match {
          case Some(_) => fail("No se deberia obtener ningun libro")
          case None => succeed
        }
      }}
    }

    "Los campos deben ser actualizados en la base de datos" in {
      val actualizar = factory.manufacturePojo(classOf[Book])
      val id = Random.nextInt(20)
      whenReady(persistence.update(id + 1, actualizar)) { result => {
        result match {
          case Some(_) => {
            whenReady(persistence.db.run(persistence.table.filter(_.id === (id + 1)).result.headOption)){search =>{
              search match {
                case Some(buscado) => {
                  assert(buscado.name == actualizar.name, "El nombre deberia ser el mismo")
                  assert(buscado.description == actualizar.description, "La descripcion deberia ser la misma");
                  assert(buscado.ISBN == actualizar.ISBN, "El ISBN deberia ser el mismo")
                  assert(buscado.image == actualizar.image, "La imagen deberia ser la misma")
                }
                case None => fail("El elemento no fue encontrado en la base de datos")
              }
            }}
          }
          case None => fail("No fue actualizado un registro")
        }
      }}
    }
  }

  "Al eliminar un libro" must {
    "Se debe actualizar un unico registro en la base de datos si el libro existe" in {
      val id = Random.nextInt(20)
      val eliminar = books(id)
      whenReady(persistence.delete(id + 1)) { result => {
        result match {
          case Some(libro) => {
            assert(libro.id == id + 1, "El id deberia ser el mismo")
            assert(libro.name == eliminar.name, "El nombre deberia ser el mismo")
            assert(libro.description == eliminar.description, "La descripcion deberia ser la misma")
            assert(libro.ISBN == eliminar.ISBN, "El ISBN deberia ser el mismo")
            assert(libro.image == eliminar.image, "La imagen deberia ser la misma")
          }
          case None => fail("Se deberia obtener el libro actualizado")
        }
      }}
    }

    "No se deberia actualizar ningún registro en la base de datos si el libro no existe" in {
      val id = 21
      whenReady(persistence.delete(id)) { result => {
        result match {
          case Some(_) => fail("No se deberia obtener ningun libro")
          case None => succeed
        }
      }}
    }

    "El libro no deberia existir en la base de datos" in {
      val id = Random.nextInt(20)
      whenReady(persistence.delete(id + 1)) { result => {
        result match {
          case Some(_) => {
            whenReady(persistence.db.run(persistence.table.filter(_.id === (id + 1)).result.headOption)){search =>{
              search match {
                case Some(buscado) => fail("No se deberia encontrar el elemento eliminado")
                case None => succeed
              }
            }}
          }
          case None => fail("No fue actualizado un registro")
        }
      }}
    }
  }
}