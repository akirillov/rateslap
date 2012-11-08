package org.awsm.rscore

import org.awsm.rscommons.{AuthObject, StatsResponse, StatsRequest}


/**
 * Created by: akirillov
 * Date: 10/21/12
 */

trait ParsingDispatcher {
  def getData(request: StatsRequest): StatsResponse = {
    getData(request.application, request.store, request.rankType, request.dates, request.countries, request.auth)
  }

  def getData(application: String, store: String, rankType: String, date: List[String], country: Set[String], auth: AuthObject): StatsResponse
}
