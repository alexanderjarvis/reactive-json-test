name := "reactive-json-test"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.2"

libraryDependencies ++= Seq(
  "com.typesafe.play.extras" %% "iteratees-extras" % "1.4.0"
)