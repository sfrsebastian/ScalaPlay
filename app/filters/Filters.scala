/*
 * Desarrollado por: Sebastián Flórez
 * Universidad de los Andes
 * Ingeniería de Sistemas y Computación
 * Pregrado
 */
package filters

import com.google.inject.Inject
import play.api.http.DefaultHttpFilters

/**
 * Inicializa los filtros de la aplicación
 */
class Filters @Inject() (security: SecurityFilter) extends DefaultHttpFilters(security)
