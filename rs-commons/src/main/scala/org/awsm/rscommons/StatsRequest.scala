package org.awsm.rscommons

/**
 * Created by: akirillov
 * Date: 10/17/12
 */

class StatsRequest(val methodName: String,  val application: String, val date: String,  val store: String, val region: String, val auth: AuthObject) extends JsonWrapper {
  def getAuth(): AuthObject = auth
}

