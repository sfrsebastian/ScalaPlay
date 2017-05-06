name := "BookStore"

version := "1.0"

lazy val CrudModule = (project in file("modules/util/CrudModule"))

lazy val AuthenticationModule = (project in file("modules/util/AuthenticationModule")).enablePlugins(PlayScala).dependsOn(CrudModule)

lazy val Models = (project in file("modules/util/Models")).dependsOn(CrudModule)

lazy val Persistence = (project in file("modules/util/Persistence")).dependsOn(Models)

lazy val Logic = (project in file("modules/util/Logic")).dependsOn(Persistence)

lazy val AuthorModule = (project in file("modules/AuthorModule")).enablePlugins(PlayScala).dependsOn(AuthenticationModule, Logic)

lazy val BookModule = (project in file("modules/BookModule")).enablePlugins(PlayScala).dependsOn(AuthenticationModule, Logic, CommentModule, AuthorModule)

lazy val CommentModule = (project in file("modules/CommentModule")).enablePlugins(PlayScala).dependsOn(AuthenticationModule, Logic)

lazy val EditorialModule = (project in file("modules/EditorialModule")).enablePlugins(PlayScala).dependsOn(AuthenticationModule, Logic)

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

concurrentRestrictions in Global += Tags.limit(Tags.Test, 1)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"