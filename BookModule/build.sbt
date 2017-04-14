name := "BookModule"

version := "1.0"

scalaVersion := "2.11.7"

routesGenerator := InjectedRoutesGenerator

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-results")

javaOptions in Test += "-Dconfig.file=../conf/application.test.conf"

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"