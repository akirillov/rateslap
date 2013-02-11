name := "rs-client"

organization := "org.awsm.rsclient"

version := "0.0.1"

scalaVersion := "2.10.0"

scalaBinaryVersion := "2.10"

scalaVersion in ThisBuild := "2.10.0"

resolvers +=  "Spray Repo" at "http://repo.spray.io/"

libraryDependencies += "io.spray" %% "spray-json" % "1.2.3"

ivyXML :=
  <dependencies>
    <dependency org="org.apache.httpcomponents" name="httpclient" rev="4.2.3"></dependency>
  </dependencies>

  seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

  exportJars := true
