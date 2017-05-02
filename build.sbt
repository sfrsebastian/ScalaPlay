name := "BookStore"

version := "1.0"

lazy val CrudModule = (project in file("modules/util/CrudModule"))

lazy val AuthenticationModule = (project in file("modules/util/AuthenticationModule")).enablePlugins(PlayScala).dependsOn(CrudModule)

lazy val BookStoreModels = (project in file("modules/util/BookStoreModels")).enablePlugins(PlayScala).dependsOn(CrudModule)

//lazy val AuthorModule = (project in file("modules/AuthorModule")).enablePlugins(PlayScala).dependsOn(AuthenticationModule, BookStoreModels)

lazy val BookModule = (project in file("modules/BookModule")).enablePlugins(PlayScala).dependsOn(AuthenticationModule, BookStoreModels, CommentModule)

lazy val CommentModule = (project in file("modules/CommentModule")).enablePlugins(PlayScala).dependsOn(AuthenticationModule, BookStoreModels)

//lazy val EditorialModule = (project in file("modules/EditorialModule")).enablePlugins(PlayScala).dependsOn(AuthenticationModule, BookStoreModels)

lazy val `bookstore` = (project in file(".")).enablePlugins(PlayScala, SonarRunnerPlugin)
  .aggregate(
    AuthenticationModule,
    BookStoreModels,
    //AuthorModule,
    BookModule,
    CommentModule
    //EditorialModule
  )
  .dependsOn(
    AuthenticationModule,
    BookStoreModels,
    //AuthorModule,
    BookModule,
    CommentModule
    //EditorialModule
  )

scalaVersion := "2.11.7"

routesGenerator := InjectedRoutesGenerator

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"