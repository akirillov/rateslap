package logic.domain

import play.api.libs.json.Json._
import play.api.Logger
import play.api.libs.json._
import play.api.libs.functional.syntax._
import logic.exception.JsonParamsException
import org.awsm.rscommons.{StatsResponse, AuthObject, StatsRequest}
import java.text.{ParseException, SimpleDateFormat}
import java.util.{Calendar, Date}

/**
 * Class represents JSON request parsing logic and forming StatsRequest.
 * Throws exception in case of wrong or absent parameters set.
 */
object RequestBuilder{

  private val format = new SimpleDateFormat("yyyy-MM-dd")

  def getIdFromRequest(json: JsValue): String = getParameterFromJson(json, "id")

  def buildRequestFromJson(json: JsValue): StatsRequest = {

    //TODO: remove this comment to documentation
    /*TEST WITH

    curl -v -H "Content-Type: application/json" -X GET -d '{"jsonrpc": "2.0", "method": "getGamesStats","params": {"application":"Cut The Rope", "store":"appstore", "dates":["2012-01-01","20120-01-02"],"rankType":"inapp", "countries":["USA","Canada"], "authObject":{"username":"user", "password":"secret"}}, "id": "1"}' http://localhost:9000/rpc.json

    */

    //TODO: move JSON parsing to commons, it is already implemented there with Spray-Json

    val params = (json  \ "params" ).asOpt[JsValue] match {
      case Some(value) => value
      case None => throw JsonParamsException("No \"params\" field found in JSON request!")
    }

    val application = getParameterFromJson(params, "application")
    val store = getParameterFromJson(params, "store")
    val rankType = getParameterFromJson(params, "rankType")

    val dates = getListFromJson(params, "dates")
    validateDates(dates)

    val countries = getSetFromJson(params, "countries")

    val authObject = getAuthObjectFromJson(params)

    new StatsRequest(application, store, rankType, dates, countries, authObject)
  }


  def getParameterFromJson(json: JsValue, fieldName: String): String = {
    (json \ fieldName).asOpt[String] match {
      case Some(value) => value
      case None => throw JsonParamsException("Parameter \""+fieldName+"\"` not specified in request!")
    }
  }

  def getListFromJson(json: JsValue, fieldName: String): List[String] = {
    (json \ fieldName).asOpt[List[String]] match {
      case Some(value) => value
      case None => throw JsonParamsException("Parameters \""+fieldName+"\"` not specified in request!")
    }
  }

  def getSetFromJson(json: JsValue, fieldName: String): Set[String] = {
    (json \ fieldName).asOpt[Set[String]] match {
      case Some(value) => value
      case None => throw JsonParamsException("Parameters \""+fieldName+"\"` not specified in request!")
    }
  }

  def getAuthObjectFromJson(json: JsValue): AuthObject = {

    val auth: JsValue = (json \ "authObject").asOpt[JsValue] match {
      case Some(value) => value
      case None => throw JsonParamsException("Auth Object not specified in request!")
    }

    new AuthObject(getParameterFromJson(auth, "username"),getParameterFromJson(auth, "password"))
  }


  def buildJsonResponse(request: StatsResponse, id: String): JsValue = {
    JsObject(
      Seq(
        "jsonrpc" -> JsString("2.0"),
        "result" -> JsObject(
          Seq(
            "application" -> JsString(request.application),
            "store" -> JsString(request.store),
            "rankType" -> JsString(request.rankType),

            if(request.rankings != null) { "rankings" -> Json.toJson (request.rankings)} else {"rankings" -> JsNull},

            "error" -> JsString(request.error)
          )
        ),
        "id" -> JsString(id)
      )
    )
  }

  def validateDates(dates: List[String]) {
    dates.foreach(date =>
      try{
        val date = format.parse(date)
        if(!date.before(new Date())) throw JsonParamsException("The date from far future provided in request!")
      } catch {
        case e: ParseException => throw JsonParamsException("Invalid date provided in request!")
      }
    )
  }
}