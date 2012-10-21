package org.awsm.rscore

import org.awsm.rscommons.{StatsResponse, StatsRequest}


/**
 * Created by: akirillov
 * Date: 10/21/12
 */

trait SourceParser {
      def getData(request: StatsRequest): StatsResponse
}
