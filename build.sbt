name := "Kinbote"

organization := "com.github.wpm.kinbote"

version := "1.0.0"

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions")

initialCommands in console := "import com.github.wpm.kinbote._"

libraryDependencies ++= Seq(
  "com.assembla.scala-incubator" % "graph-core_2.10" % "1.8.1",
  "org.scalatest" %% "scalatest" % "2.1.0" % "test"
)
