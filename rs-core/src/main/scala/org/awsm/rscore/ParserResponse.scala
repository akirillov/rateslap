package org.awsm.rscore

import org.awsm.rscommons.StatsResponse

/**
 * Created by: akirillov
 * Date: 10/23/12
 */

class ParserResponse (response: StatsResponse, error: String) {

  def getData(): StatsResponse = response

  def getError(): String = error

  def hasError() = {
    error != null
  }

}
