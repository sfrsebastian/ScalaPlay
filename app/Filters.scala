import com.google.inject.Inject
import filters.SecurityFilter
import play.api.http.DefaultHttpFilters

/**
  * Created by sfrsebastian on 4/20/17.
  */
class Filters @Inject() (security: SecurityFilter) extends DefaultHttpFilters(security)
