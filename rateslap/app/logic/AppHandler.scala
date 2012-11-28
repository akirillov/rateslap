package logic

import domain.RequestBuilder
import models._
import com.codahale.jerkson.Json
import play.api.libs.json._
import play.Logger

/**
 * Created by: akirillov
 * Date: 10/16/12
 */

object AppHandler {

  def getGameStats(request: JsValue): JsValue = {

    val statsRequest = RequestBuilder.buildRequestFromJson(request)

    Logger.info("stats request created: "+statsRequest.toString())
    //Rank.find()

    //Rank.find()

    //here we will pass built object to library and will wait for response
    //Akka will be hidden in those library



    request
  }

  def getMultiGamesStats(request: JsValue): JsValue = {
     request
  }
}
