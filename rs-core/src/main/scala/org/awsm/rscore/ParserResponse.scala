package org.awsm.rscore

import org.awsm.rscommons.StatsResponse

/**
 * Created by: akirillov
 * Date: 10/23/12
 */

class ParserResponse (val response: StatsResponse, val error: String) {
  def hasError: Boolean = {
    error != null
  }

}
