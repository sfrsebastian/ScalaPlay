name := "BookStore"

version := "1.0"

lazy val CommonModule = (project in file("CommonModule")).enablePlugins(PlayScala)

lazy val BookModule = (project in file("BookModule")).enablePlugins(PlayScala).dependsOn(CommonModule)

lazy val `bookstore` = (project in file(".")).enablePlugins(PlayScala, SonarRunnerPlugin).aggregate(CommonModule, BookModule).dependsOn(CommonModule, BookModule)

scalaVersion := "2.11.7"

routesGenerator := InjectedRoutesGenerator

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"