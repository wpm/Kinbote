name := "Kinbote"

organization := "com.github.wpm.kinbote"

version := "1.0.0"

scalaVersion := "2.11.0"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions")

initialCommands in console := "import com.github.wpm.kinbote._"

libraryDependencies ++= Seq(
  "com.assembla.scala-incubator" % "graph-core_2.11" % "1.8.1",
  "edu.arizona.sista" % "processors" % "2.0",
  "org.scalatest" % "scalatest_2.11" % "2.1.5" % "test"
)
