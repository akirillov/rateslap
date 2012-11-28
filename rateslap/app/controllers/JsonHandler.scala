package controllers

import play.api.mvc._
import play.api.libs.json.JsValue
import logic._
import util.control.Exception.Catch
import play.Logger

/**
 * Created by: akirillov
 * Date: 10/16/12
 */

object JsonHandler extends  Controller{

  def handleJsonRequest =  Action(parse.json) {
      request =>
        (request.body \ "method").asOpt[String].map { method => method match {
          case "getGamesStats" => Ok(AppHandler.getGameStats(request.body))
//          case "getMultiGamesStats" =>  Ok(AppHandler.getMultiGamesStats(request.body))

          case _ =>  Ok(ErrorConstructor.constructError((request.body \ "id").asOpt[String], -32601, "Method "+method+" not found"))
        }
        }.getOrElse {
          BadRequest(ErrorConstructor.constructError((request.body \ "id").asOpt[String], -32601, "Method not found"))
        }
  }
}
