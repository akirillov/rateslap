package org.awsm.rscore

import appannie.AppAnnieParser
import org.awsm.rscommons.{AuthObject, StatsRequest}


object TestRunner extends App {
  val parser = new AppAnnieParser
  val response = parser.getData(StatsRequest("someMethod",  "MegaStore", "SouthAfrica", AuthObject("user", "secret")))
  println(response)
  println("Resp JSON: "+response.generateJson())
}
