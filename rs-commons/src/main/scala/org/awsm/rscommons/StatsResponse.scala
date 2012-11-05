package org.awsm.rscommons

/**
 * Created by: akirillov
 * Date: 10/17/12
 */

class StatsResponse(val date: String,  val appName: String,  val country: String,  val rate: String, val error: String) extends JsonWrapper{
  def this(date: String,  appName: String, country: String,  rate: String){
    this(date, appName, country, rate, null)
  }

  def this(error: String){
    this(null,  null, null,  null, error)
  }

  def hasError: Boolean = error != null
}

