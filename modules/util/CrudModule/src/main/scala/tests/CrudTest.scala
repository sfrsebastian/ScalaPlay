/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package crud.tests

/**
  * Created by sfrsebastian on 4/13/17.
  */
trait CrudTest {
  def createTest:Unit
  def getTest:Unit
  def getAllTest:Unit
  def updateTest:Unit
  def deleteTest:Unit

  createTest
  getTest
  getAllTest
  updateTest
  deleteTest
}
