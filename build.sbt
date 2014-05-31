name := "Kinbote"

organization := "com.github.wpm.kinbote"

version := "1.0.0"

scalaVersion := "2.11"

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-language:implicitConversions",
  "-language:existentials"
)

initialCommands in console := "import com.github.wpm.kinbote._"

resolvers += "spray" at "http://repo.spray.io/"

libraryDependencies ++= Seq(
  "edu.arizona.sista" % "processors" % "2.0",
  "com.gensler" % "scalavro-util_2.10" % "0.6.2",
  "org.scalatest" %% "scalatest" % "2.1.5" % "test"
)
