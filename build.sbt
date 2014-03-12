name := "Kinbote"

organization := "com.github.wpm.Kinbote"

version := "1.0.0"

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-unchecked", "-deprecation")

initialCommands in console := "import com.github.wpm.Kinbote._"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.1.0" % "test"
