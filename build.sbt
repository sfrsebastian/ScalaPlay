name := "BookStore"

version := "1.0"

lazy val CrudModule = (project in file("modules/util/CrudModule"))

lazy val AuthenticationModule = (project in file("modules/util/AuthenticationModule")).enablePlugins(PlayScala).dependsOn(CrudModule)

lazy val Models = (project in file("modules/Models")).dependsOn(CrudModule)

lazy val Persistence = (project in file("modules/Persistence")).dependsOn(Models)

lazy val Logic = (project in file("modules/Logic")).dependsOn(Persistence)

lazy val AuthorModule = (project in file("modules/Services/AuthorModule")).enablePlugins(PlayScala).dependsOn(AuthenticationModule, Logic, BookModule)

lazy val BookModule = (project in file("modules/Services/BookModule")).enablePlugins(PlayScala).dependsOn(AuthenticationModule, Logic, CommentModule)

lazy val CommentModule = (project in file("modules/Services/CommentModule")).enablePlugins(PlayScala).dependsOn(AuthenticationModule, Logic)

lazy val EditorialModule = (project in file("modules/Services/EditorialModule")).enablePlugins(PlayScala).dependsOn(AuthenticationModule, Logic, BookModule)

lazy val `bookstore` = (project in file(".")).enablePlugins(PlayScala, SonarRunnerPlugin)
  .aggregate(
    AuthenticationModule,
    Models,
    Persistence,
    Logic,
    AuthorModule,
    BookModule,
    CommentModule,
    EditorialModule
  )
  .dependsOn(
    AuthenticationModule,
    Models,
    Persistence,
    Logic,
    AuthorModule,
    BookModule,
    CommentModule,
    EditorialModule
  )

scalaVersion := "2.11.7"

routesGenerator := InjectedRoutesGenerator

//Solo se ejecuta una prueba a la vez
concurrentRestrictions in Global += Tags.limit(Tags.Test, 1)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"