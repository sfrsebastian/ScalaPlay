name := "BookStore"

version := "1.0"

lazy val BookModule = (project in file("BookModule")).enablePlugins(PlayScala)

lazy val `bookstore` = (project in file(".")).enablePlugins(PlayScala).dependsOn(BookModule).aggregate(BookModule)

scalaVersion := "2.11.7"

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"  