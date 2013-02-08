package org.awsm.rsclient

import json.{RSResponseProtocol, RSResponse, RSRequestProtocol, RSRequest}
import org.awsm.rscommons.{StatsRequest, StatsResponse}
import spray.json._

/**
 * Created by: akirillov
 * Date: 2/5/13
 */

object JsonBuilder {
    def buildJSONResponse(response: RSResponse): String = {
      import RSResponseProtocol._
      response.toJson.toString()
    }
  
    def buildJSONRequest(request: RSRequest): String = {
      import RSRequestProtocol._
      request.toJson.toString()
    }

  def buildResponseFromJSON(response: String): RSResponse = {
    import RSResponseProtocol._
    response.asJson.convertTo[RSResponse]
  }

  def buildRequestFromJSON(request: String): RSRequest = {
    import RSRequestProtocol._
    request.asJson.convertTo[RSRequest]
  }
}
