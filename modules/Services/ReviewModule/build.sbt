name := "ReviewModule"

version := "1.0"

scalaVersion := "2.11.7"

routesGenerator := InjectedRoutesGenerator

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-results")

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"