name := "CommonModule"

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
  "uk.co.jemos.podam" % "podam" % "6.0.1.RELEASE",
  "com.mohiva" %% "play-silhouette" % "4.0.0" intransitive,
  "com.mohiva" %% "play-silhouette-password-bcrypt" % "4.0.0" intransitive,
  "com.mohiva" %% "play-silhouette-crypto-jca" % "4.0.0" intransitive,
  "com.mohiva" %% "play-silhouette-persistence" % "4.0.0" intransitive,
  "com.mohiva" %% "play-silhouette-testkit" % "4.0.0" % "test" intransitive,
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