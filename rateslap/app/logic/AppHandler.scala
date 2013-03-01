package logic

import concurrent.{ManagerActor}
import domain.RequestBuilder
import exception.JsonParamsException
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
    RequestBuilder.getIdFromRequest(request) match {
      case None =>  ErrorConstructor.constructError(null, -32600, "Invalid JSON was received by the server. ID is required by JSON-RPC 2.0 protocol, but missed from request body!")
      case Some(id) => {

        val requestId = id

        try{
          val statsRequest = RequestBuilder.buildRequestFromJson(request)

          Logger.info("Request captured: "+statsRequest.toString())

          val manager = new ManagerActor
          manager.start()

          val future =  manager !? statsRequest

          future match {
            case resp: StatsResponse => Logger.info("Response created: "+resp); RequestBuilder.buildJsonResponse(resp, requestId)
            case t: Throwable => RequestBuilder.buildJsonResponse(new StatsResponse(t.getMessage), requestId)
            case _ => RequestBuilder.buildJsonResponse(new StatsResponse("Unknown error occured. No data. See server log for details."), requestId)
          }
        } catch {
          case p: JsonParamsException => RequestBuilder.buildJsonResponse(new StatsResponse(p.getMessage), requestId)
        }
      }
    }
  }

}
