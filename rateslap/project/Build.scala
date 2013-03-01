import sbt._
import Keys._
import PlayProject._
import sbt.RootProject._

object ApplicationBuild extends Build {

  val appName         = "rateslap"
  val appVersion      = "1.0"

  scalaVersion := "2.10.0"
  scalaBinaryVersion := "2.10"

  val appDependencies = Seq(
    "org.awsm.rscore" % "rs-core_2.10" % "0.0.1",
    "net.sourceforge.htmlunit" % "htmlunit" % "2.11",
    "org.awsm.rscommons" % "rs-commons_2.10" % "0.0.1",
    "mysql" % "mysql-connector-java" % "5.1.18",
    "org.scala-lang" % "scala-compiler" % "2.10.0",
    "org.scala-lang" % "scala-actors" % "2.10.0",
    jdbc, anorm, javaCore, javaJdbc
  )

  lazy  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalaVersion:="2.10.0",
    scalaBinaryVersion := "2.10",
    scalaVersion in ThisBuild := "2.10.0",

    resolvers += (Resolver.file("Local Ivy Repository", file(Path.userHome.absolutePath+"/.ivy2/local"))(Resolver.ivyStylePatterns))
  )
}
