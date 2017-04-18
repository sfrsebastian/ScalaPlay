name := "BookStore"

version := "1.0"

lazy val CrudModule = (project in file("modules/CrudModule"))

lazy val AuthModule = (project in file("modules/AuthModule")).enablePlugins(PlayScala).dependsOn(CrudModule)

lazy val BookModule = (project in file("modules/BookModule")).enablePlugins(PlayScala).dependsOn(AuthModule)

lazy val `bookstore` = (project in file(".")).enablePlugins(PlayScala, SonarRunnerPlugin).aggregate(AuthModule, BookModule).dependsOn(AuthModule, BookModule)

scalaVersion := "2.11.7"

routesGenerator := InjectedRoutesGenerator

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"