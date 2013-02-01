package logic

import concurrent.{ManagerActor}
import domain.RequestBuilder
import play.api.libs.json._
import play.Logger
import org.awsm.rscommons.StatsResponse
import models.Rank
import models.Rank._
import anorm.NotAssigned

/**
 * Created by: akirillov
 * Date: 10/16/12
 */

object AppHandler {

  def getGameStats(request: JsValue): JsValue = {

    val statsRequest = RequestBuilder.buildRequestFromJson(request)

    Logger.info("stats request created: "+statsRequest.toString())

   val manager = new ManagerActor
    manager.start()

    val future =  manager !? statsRequest

    future match {
      case resp: StatsResponse => Logger.info("RESPONSE CAPTURED! "+resp); RequestBuilder.buildJsonResponse(resp)
      case _ => RequestBuilder.buildJsonResponse(new StatsResponse("Unknown error occured. No data. See server log for details."))
    }
  }

  def getMultiGamesStats(request: JsValue): JsValue = {
    request
  }
}
