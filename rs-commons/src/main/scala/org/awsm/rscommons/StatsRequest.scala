package org.awsm.rscommons

/**
 * Created by: akirillov
 * Date: 10/17/12
 */

case class StatsRequest(methodName: String,  store: String, region: String, auth: AuthObject) extends JsonWrapper {
  def getAuth(): AuthObject = auth
}

