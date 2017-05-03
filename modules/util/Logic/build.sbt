name := "Logic"

version := "1.0"

scalaVersion := "2.11.7"

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-results")

resolvers += "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases"