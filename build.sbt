name := "Kinbote"

organization := "com.github.wpm"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.10.3"

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-language:implicitConversions",
  "-language:existentials"
)

initialCommands in console := "import com.github.wpm.kinbote._"

libraryDependencies ++= Seq(
  "edu.arizona.sista" % "processors" % "2.0",
  "com.gensler" % "scalavro_2.10" % "0.6.2",
  "org.scalatest" %% "scalatest" % "2.2.0" % "test"
)
