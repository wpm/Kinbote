name := "Kinbote"

organization := "com.github.wpm.kinbote"

version := "1.0.0"

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-unchecked", "-deprecation")

initialCommands in console := "import com.github.wpm.kinbote._"

libraryDependencies += "com.assembla.scala-incubator" % "graph-core_2.10" % "1.8.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.1.0" % "test"
