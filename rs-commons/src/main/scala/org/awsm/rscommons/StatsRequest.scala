package org.awsm.rscommons

import collection.immutable.HashSet

/**
 * Request container.
 * Contains:
 * - single parameters such as method name, application name, store
 * - collections: dates list and countries set
 * - authorization object.
 *
 * Provides unified request parameter to wrap all parameters combinations in single class.
 *
 * Created by: akirillov
 * Date: 10/17/12
 */

class StatsRequest(val methodName: String,  val application: String, val store: String, val date: List[String],  val country: Set[String], val auth: AuthObject) extends JsonWrapper {

  def this(methodName: String,  application: String, store: String, date: String, country: Set[String], auth: AuthObject) = {
    this(methodName,  application, store, date::List(), country, auth)
  }

  def this(methodName: String,  application: String, store: String, date: List[String], country: String, auth: AuthObject) = {
    this(methodName,  application, store, date, Set()+country, auth)
  }
}

