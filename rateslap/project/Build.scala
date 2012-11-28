import sbt._
import Keys._
import PlayProject._
import sbt.RootProject._

object ApplicationBuild extends Build {

  val appName         = "rateslap"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.awsm.rscore" % "rs-core_2.9.2" % "0.0.1",
    "org.awsm.rscommons" % "rs-commons_2.9.2" % "0.0.1",
    "mysql" % "mysql-connector-java" % "5.1.18"
  )

  lazy  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    // Add your own project settings here
  )
}
