package org.awsm.rscore

import appannie.AppAnnieDispatcher
import org.awsm.rscommons.{AuthObject, StatsRequest}


object TestRunner extends App {
  val dispatcher = new AppAnnieDispatcher

  val response = dispatcher.getData(StatsRequest("someMethod",  "MegaStore", "SouthAfrica", AuthObject("user", "secret")))

  println("Resp JSON: "+response.generateJson())
}
