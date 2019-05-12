import Dependencies._

addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.0-M4")

ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "alandevlin",
      scalaVersion := "2.12.8",
    )),
  name := "functional-hangman",
  libraryDependencies ++= Seq(scalaz,aws,scalaTest % Test)
)

