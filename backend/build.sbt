name := "issue-tracker"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "42.1.4",
  "com.typesafe.play" %% "play-slick" % "3.0.0",
  "com.github.t3hnar" %% "scala-bcrypt" % "3.0",
  "com.pauldijou" %% "jwt-play-json" % "0.14.1",
  "com.github.nscala-time" %% "nscala-time" % "2.18.0"
)

libraryDependencies += filters