name := "CrudModule"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.2.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.0",
  "com.typesafe.slick" %% "slick-codegen" % "3.2.0",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0",
  "org.mockito" % "mockito-core" % "2.7.22",
  "uk.co.jemos.podam" % "podam" % "6.0.1.RELEASE"
)

resolvers += "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases"