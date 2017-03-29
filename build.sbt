name := "BookStore"

version := "1.0"

lazy val BookModule = (project in file("BookModule")).enablePlugins(PlayScala, SonarRunnerPlugin)

lazy val `bookstore` = (project in file(".")).enablePlugins(PlayScala).dependsOn(BookModule).aggregate(BookModule)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0" % Test
)

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-results")
//unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

//coverageEnabled := true

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"