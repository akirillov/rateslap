package logic

import domain.RequestBuilder
import com.codahale.jerkson.Json
import play.api.libs.json.{Json, JsValue}

/**
 * Created by: akirillov
 * Date: 10/16/12
 */

object AppHandler {

  def getSingleDayStats(request: JsValue): JsValue = {

    RequestBuilder.buildRequestFromJson(request)

    //here we will pass built object to library and will wait for response
    //Akka will be hidden in those library



    request
  }

  def getMultiGamesStats(request: JsValue): JsValue = {
     request
  }
}
