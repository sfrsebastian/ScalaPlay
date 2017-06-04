/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package settings

import com.google.inject.AbstractModule

/**
  * Modulo principal de la aplicación
  * Inicializa la clase OnStartup para realizar la creación de tablas de base de datos.
  */
class BookStoreModule extends AbstractModule {
  override def configure() = {
    bind(classOf[OnStartup]).asEagerSingleton();
  }
}
