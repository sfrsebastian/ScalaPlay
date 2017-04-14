name := "BookStore"

version := "1.0"

lazy val CommonModule = (project in file("CommonModule")).enablePlugins(PlayScala)
lazy val BookModule = (project in file("BookModule")).enablePlugins(PlayScala, SonarRunnerPlugin).aggregate(CommonModule).dependsOn(CommonModule)

lazy val `bookstore` = (project in file(".")).enablePlugins(PlayScala).aggregate(BookModule).dependsOn(BookModule)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0"
)

routesGenerator := InjectedRoutesGenerator

//testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-results")

//javaOptions in Test += "-Dconfig.file=conf/application.test.conf"

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"