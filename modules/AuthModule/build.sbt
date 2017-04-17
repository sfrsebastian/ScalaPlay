name := "AuthModule"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.mohiva" %% "play-silhouette" % "4.0.0" intransitive,
  "com.mohiva" %% "play-silhouette-password-bcrypt" % "4.0.0" intransitive,
  "com.mohiva" %% "play-silhouette-crypto-jca" % "4.0.0" intransitive,
  "com.mohiva" %% "play-silhouette-persistence" % "4.0.0" intransitive,
  "net.codingwell" %% "scala-guice" % "4.0.0",
  "com.iheart" %% "ficus" % "1.2.6",
  "com.typesafe.play" %% "play-mailer" % "5.0.0",
  "com.adrianhurt" %% "play-bootstrap3" % "0.4.4-P24",
  "org.webjars" %% "webjars-play" % "2.4.0",
  "org.mindrot" % "jbcrypt" % "0.3m"
)

routesGenerator := InjectedRoutesGenerator

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-results")

javaOptions in Test += "-Dconfig.file=../conf/database.test.conf"

resolvers ++= Seq(
  "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases",
  "Atlassian Releases" at "https://maven.atlassian.com/public/",
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/releases/"
)