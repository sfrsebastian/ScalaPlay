name := "BookStore"

version := "1.0"

lazy val book = (project in file("BookModule")).enablePlugins(PlayScala)

lazy val `bookstore` = (project in file(".")).enablePlugins(PlayScala).dependsOn(book).aggregate(book)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"  