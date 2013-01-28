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

class StatsRequest(val application: String, val store: String, val rankType: String, val dates: List[String],  val countries: Set[String], val auth: AuthObject) extends JsonWrapper {

  def this(application: String, store: String, rankType: String, date: String, countries: Set[String], auth: AuthObject) = {
    this(application, store, rankType, date::List(), countries, auth)
  }

  def this(application: String, store: String, rankType: String, dates: List[String], country: String, auth: AuthObject) = {
    this(application, store, rankType, dates, Set()+country, auth)
  }
}

