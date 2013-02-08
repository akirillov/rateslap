name := "rs-commons"

publishMavenStyle := true

organization := "org.awsm.rscommons"

version := "0.0.1"

scalaVersion := "2.10.0"

scalaBinaryVersion := "2.10"

scalaVersion in ThisBuild := "2.10.0"

resolvers += "repo.codahale.com" at "http://repo.codahale.com"

libraryDependencies += "com.codahale" % "jerkson_2.9.1" % "0.5.0"