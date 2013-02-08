package org.awsm.rsclient

import client.ServiceMethods
import json.{RSResponse, RSRequest}
import org.awsm.rscommons.{StatsResponse, AuthObject, StatsRequest}
import spray.json._
/**
 * Created by: akirillov
 * Date: 2/5/13
 */

object Test extends App {
  val json = JsonBuilder.buildJSONRequest(
    RSRequest(
      jsonrpc = "2.0",
      method = ServiceMethods.APPANNIE_GET_GAMES_STATS.toString,
      id = System.currentTimeMillis().toString,

        params = new StatsRequest(
        application = "Cut the Rope",
        store = "appstore",
        rankType = "ranks",
        dates = List("2012-01-01", "2012-02-02"),
        countries = Set("United States", "Russia"),
        authObject = AuthObject("user", "secret")
        )
    )
  )
  
  println("REQUEST TEST:\n")
  
  println(json)

  val request = JsonBuilder.buildRequestFromJSON(json)

  println(request)


  println("\n\nRESPONSE TEST: \n")

  val resp = """
  {"jsonrpc":"2.0","result":{"application":"Cut The Rope","store":"appstore","rankType":"ranks","rankings":{"2012-05-01":{"Canada":"11","United States":"9"},"2012-10-03":{"Canada":"17","United States":"27"},"2012-03-02":{"Canada":"17","United States":"27"}},"error":null},"id":"1"}
  """

  val obj = JsonBuilder.buildResponseFromJSON(resp)

  println(obj)

  println(JsonBuilder.buildJSONResponse(obj))
}
