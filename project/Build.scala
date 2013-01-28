import sbt._
import Keys._
import scala.xml._

object RateSlapBuild extends Build {

 lazy val rateslap = Project(id = "rsws", base = file("rateslap")) dependsOn(rscore, rscommons) aggregate (rscore, rscommons)

 lazy val rscore = Project(id = "rscore", base = file("rs-core")) dependsOn(rscommons)

 lazy val rscommons = Project(id = "rscommons", base = file("rs-commons"))


  lazy val root = Project(id = "rateslap", base = file(".")) aggregate(rateslap, rscore, rscommons)

  //publishTo := Some(Resolver.file("Local SBT Repository", file(Path.userHome.absolutePath+"/.ivy2/local"))(Resolver.ivyStylePatterns))

  /*
   val common  = Project("hi-common", file("common"))

    val client1 = Project("hi-client1", file("client1")).dependsOn(common)

    val client2 = Project("hi-client2", file("client2")).dependsOn(common)

    val webapp = PlayProject("hi-webapp", appVersion, appDependencies, path = file("webapp"),  mainLang = JAVA).settings(
        // Add your own project settings here
    ).dependsOn(common)

    val root = Project("hi", file(".")).aggregate(client1, client2, webapp)



  */
}
