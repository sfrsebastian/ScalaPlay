name := "Persistence"

version := "1.0"

scalaVersion := "2.11.7"

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-results")

javaOptions in Test += "-Dconfig.file=../../conf/application.conf -Dconfig.file=../../conf/database.test.conf"

resolvers += "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases"