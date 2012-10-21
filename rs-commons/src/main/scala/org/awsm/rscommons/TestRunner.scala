package org.awsm.rscommons

/**
 * Created by: akirillov
 * Date: 10/17/12
 */

object TestRunner extends App{

  println(StatsRequest("someMethod",  "MegaStore", "SouthAfrica", AuthObject("user", "secret")).generateJson())

}
