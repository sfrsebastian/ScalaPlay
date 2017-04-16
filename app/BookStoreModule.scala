package settings

import com.google.inject.AbstractModule

/**
  * Created by sfrsebastian on 4/16/17.
  */
class BookStoreModule extends AbstractModule {
  override def configure() = {
    bind(classOf[OnStartup]).asEagerSingleton()
  }
}
