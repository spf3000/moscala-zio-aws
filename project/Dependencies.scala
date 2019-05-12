import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  lazy val scalaz =  "org.scalaz" %% "scalaz-zio" % "1.0-RC4"
  lazy val aws = "com.amazonaws" % "aws-java-sdk-s3" % "1.11.546"
}
