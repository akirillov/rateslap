import sbt._
import Keys._
import scala.xml._

object RateSlapBuild extends Build {
lazy val root = Project(id = "rateslap",
                            base = file(".")) aggregate(rateslap, rscore, rscommons)

 lazy val rateslap = Project(id = "rateslap-ws",
                           base = file("rateslap")) dependsOn(rscore, rscommons)

 lazy val rscore = Project(id = "rateslap-core",
                           base = file("rs-core")) dependsOn(rscommons)

 lazy val rscommons = Project(id = "rateslap-commons",
                           base = file("rs-commons"))
}
