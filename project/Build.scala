import sbt._
import Keys._
import scala.xml._

object RateSlapBuild extends Build {

  scalaVersion := "2.10.0"

  scalaBinaryVersion := "2.10"

  scalaVersion in ThisBuild := "2.10.0"

  lazy val rateslap = Project(id = "rsws", base = file("rateslap")) dependsOn(rscore, rscommons) aggregate (rscore, rscommons)

  lazy val rscore = Project(id = "rscore", base = file("rs-core")) dependsOn(rscommons)

  lazy val rscommons = Project(id = "rscommons", base = file("rs-commons"))

  lazy val rsclient = Project(id = "rsclient", base = file("rs-client")) dependsOn(rscommons) aggregate (rscommons)

  lazy val root = Project(id = "rateslap", base = file(".")) aggregate(rateslap, rscore, rscommons, rsclient)
}
