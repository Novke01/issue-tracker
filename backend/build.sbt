name := "issue-tracker"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "42.1.4",
  "com.typesafe.play" %% "play-slick" % "3.0.0",
  "com.github.t3hnar" %% "scala-bcrypt" % "3.0",
  "com.pauldijou" %% "jwt-play-json" % "0.14.1",
  "com.github.nscala-time" %% "nscala-time" % "2.18.0",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "org.mockito" % "mockito-core" % "2.13.0" % Test
)

libraryDependencies += filters